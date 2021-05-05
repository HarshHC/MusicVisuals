package d18130149;

import java.util.ArrayList;
import java.util.ListIterator;

import ie.tudublin.*;
import processing.core.PFont;

public class HarshsVisual extends Visual {

    Player player;
    PFont font;
    int score, highscore, startTime, leftObstacles, rightObstacles;
    float speed, penalty = 0;
    boolean previousBeat = false;
    boolean isGameRunning, isMenuVisible = true;
    ArrayList<Obstacle> obstacles;

    public void settings() {
        size(500, 1024);
    }

    public void setup() {
        surface.setResizable(true);
        setupAudio();

        rectMode(CENTER);
        imageMode(CENTER);

        setupGame();
    }

    public void setupAudio() {
        startMinim();
        // Call loadAudio to load an audio file to process
        loadAudio("java/data/levitate.mp3");
        // startListening();
    }

    public void setupGame() {
        startTime = millis();
        player = new Player(this);
        speed = 0.1f;
        isGameRunning = false;

        String lines[] = loadStrings("java/data/highScore.txt");
        highscore = parseInt(lines[0]);

        obstacles = new ArrayList<Obstacle>();
        font = createFont("java/data/tittillium.ttf", 50);

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
            if (!isGameRunning) {
                startTime = millis();
                score = 0;
                penalty = 0;
                getAudioPlayer().cue(2000);
                getAudioPlayer().play();
                isGameRunning = true;
            } else {
                gameOver();
            }

            isMenuVisible = !isMenuVisible;
        }
    }

    public void gameOver() {
        isGameRunning = false;
        getAudioPlayer().cue(0);
        if (score > highscore) {
            highscore = score;
            String[] highscoreTxt = { str(highscore) };

            saveStrings("java/data/highScore.txt", highscoreTxt);
        }
    }

    public void draw() {
        background(0);

        renderObstacles();

        if (isMenuVisible) {
            showMenu();
            speed = 0;
        } else {
            updateScore();
            readMusicInput();

            // show the player and obstacles
            player.render();

            // update game speed
            speed = getAmplitude() * 50;

            // find beats and generate shape obstacles
            findMusicBeats();
            generateBarObstacles();

            if (!isMusicPlaying()) {
                gameOver();
                isMenuVisible = true;
            }
        }

    }

    public void showMenu() {

        fill(0, 0, 0, 90);
        rect(width / 2, height / 2, 500, 1024);
        textAlign(CENTER);
        textFont(font);
        fill(200, 100, 255);
        textSize(55);
        text("LEVITATE!", width / 2, height * 0.2f);

        fill(255, 255, 255);
        textSize(40);
        text("High Score: " + highscore, width / 2, height * 0.3f);
        text("Score: " + score, width / 2, height * 0.4f);
        textSize(20);
        text("Dodge all obstacles in the way by moving\nleft and right. Each obstacle hit reduces\nthe score by 10%. Can you beat your highscore\n by the end of the song? ",
                width / 2, height * 0.55f);

        text("Press 'SPACE' to start or restart the game", width / 2, height * 0.9f);

    }

    // update game score
    public void updateScore() {
        textAlign(LEFT);
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
                fill(200, 0, 0);
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
            generateShapeObstacles();
            previousBeat = isBeat;
        }
    }

    // function to generate game obstacles
    public void generateShapeObstacles() {

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

    }

    public void generateBarObstacles() {
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
