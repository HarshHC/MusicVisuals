package d18130149;

import java.util.ArrayList;
import java.util.ListIterator;

import ie.tudublin.*;

public class HarshsVisual extends Visual {

    Player player;
    int score, startTime, leftObstacles, rightObstacles;
    float speed, penalty = 0;
    boolean previousBeat = false;
    ArrayList<Obstacle> obstacles;

    public void settings() {
        size(500, 1024);
    }

    public void setup() {
        surface.setResizable(true);

        rectMode(CENTER);
        imageMode(CENTER);

        setupAudio();
        setupGame();
    }

    public void setupAudio() {
        startMinim();
        // Call loadAudio to load an audio file to process
        loadAudio("java/data/levitate.mp3");
        // startListening();
        setupAudio();
    }

    public void setupGame() {
        startTime = millis();
        player = new Player(this);
        speed = 0.1f;
        obstacles = new ArrayList<Obstacle>();

        generateInitialObstacles();
    }

    public void generateInitialObstacles() {
        for (int i = 0; i < 2; i++) {
            float randomObstacle = Math.round(random(0, 1));
            if (randomObstacle == 0) {
                SquareObstacle o = new SquareObstacle(this);
                obstacles.add(o);
            } else if (randomObstacle == 1) {
                CircleObstacle o = new CircleObstacle(this);
                obstacles.add(o);
            }
        }

        Obstacle leftO = new SideBarsObstacle(this, Constants.LEFT);
        Obstacle rightO = new SideBarsObstacle(this, Constants.RIGHT);
        obstacles.add(leftO);
        obstacles.add(rightO);

        leftObstacles = 1;
        rightObstacles = 1;
    }

    public void keyPressed() {
        if (key == ' ') {
            startTime = millis();
            score = 0;
            penalty = 0;
            getAudioPlayer().cue(0);
            getAudioPlayer().play();
        }
    }

    public void draw() {
        background(0);

        updateScore();
        readMusicInput();

        // show the player and obstacles
        player.render();
        renderObstacles();

        // update game speed
        speed = getAmplitude() * 50;

        findMusicBeats();
        generateObstacles();
    }

    // update game score
    public void updateScore() {
        score = (millis() - startTime) / 100 - Math.round(penalty);
        textSize(20);
        text("Score: " + score, 50, 50);
    }

    // read input from Minim
    public void readMusicInput() {
        try {
            calculateFFT();
        } catch (VisualException e) {
            e.printStackTrace();
        }

        calculateFrequencyBands();
        calculateAverageAmplitude();
    }

    // show, update and delete game obstacles
    public void renderObstacles() {
        ListIterator<Obstacle> itr = obstacles.listIterator();
        while (itr.hasNext()) {
            Obstacle obstacle = itr.next();

            // Handle collision detection and apply penalty on the score
            if (obstacle.didCollideWithPlayer(player.getCollisionBody())) {
                penalty += 0.2 * score;
            }

            // Render the different obstacle types
            if (obstacle instanceof SideBarsObstacle) {
                SideBarsObstacle ob = (SideBarsObstacle) obstacle;
                ob.show(leftObstacles, rightObstacles);
                ob.move(speed);

            } else if (obstacle instanceof SquareObstacle) {
                SquareObstacle ob = (SquareObstacle) obstacle;

                ob.show();
                ob.rotate(speed);
                ob.move(speed);

            } else if (obstacle instanceof CircleObstacle) {
                CircleObstacle ob = (CircleObstacle) obstacle;

                ob.show();
                ob.scale(speed);
                ob.move(speed);
            }

            // delete objects off screen
            if (obstacle.isOffScreen()) {
                itr.remove();
                if (obstacle instanceof SideBarsObstacle) {
                    SideBarsObstacle ob = (SideBarsObstacle) obstacle;

                    if (ob.placement == Constants.LEFT) {
                        leftObstacles--;
                    } else {
                        rightObstacles--;
                    }
                }
            }
        }

    }

    // function to find beats in music to match object generation with music
    public void findMusicBeats() {
        float[] bands = getSmoothedBands();
        float max = Integer.MIN_VALUE;
        float min = Integer.MAX_VALUE;
        boolean isBeat;

        float avg = 0;
        for (int i = 0; i < bands.length; i++) {
            avg += bands[i];
            max = Math.max(max, bands[i]);
            min = Math.min(min, bands[i]);
        }

        if ((max - (avg / bands.length)) < ((avg / bands.length) - min)) {
            isBeat = true;
        } else {
            isBeat = false;
        }

        if (isBeat != previousBeat) {
            generateObstacles();
            previousBeat = isBeat;
        }
    }

    // function to generate game obstacles
    public void generateObstacles() {

        // Generate Square and Circle obstacles, maximum based on current score
        if (obstacles.size() < map(score, 0, 2000, 0, 20)) {
            float randomObstacle = Math.round(random(0, 1));
            if (randomObstacle == 0) {
                SquareObstacle o = new SquareObstacle(this);
                obstacles.add(o);
            } else if (randomObstacle == 1) {
                CircleObstacle o = new CircleObstacle(this);
                obstacles.add(o);
            }
        }

        // Generate Side Bar obstacles if total obstacles on screen is less than 2
        if (obstacles.size() < 2) {
            float randomSide = Math.round(random(0, 1));

            if (randomSide == 0) {
                SideBarsObstacle o = new SideBarsObstacle(this, Constants.RIGHT);
                rightObstacles++;
                obstacles.add(o);
            } else if (randomSide == 1) {
                SideBarsObstacle o = new SideBarsObstacle(this, Constants.LEFT);
                leftObstacles++;
                obstacles.add(o);
            }
        }
    }

}
