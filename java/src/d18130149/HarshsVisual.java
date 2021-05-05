package d18130149;

import java.util.ArrayList;
import java.util.ListIterator;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import ie.tudublin.*;

public class HarshsVisual extends Visual {

    Player player;
    int score, startTime, leftObstacles, rightObstacles;
    float speed;
    boolean previousBeat = false;
    ArrayList<Obstacle> obstacles;

    public void settings() {
        size(500, 1024);
    }

    public void setup() {
        surface.setResizable(true);
        startMinim();
        // frameRate(5);
        // Call loadAudio to load an audio file to process
        loadAudio("java/data/levitate.mp3");
        getAudioPlayer().cue(0);
        getAudioPlayer().play();

        startTime = millis();
        player = new Player(this);
        speed = 0.1f;

        rectMode(CENTER);
        imageMode(CENTER);

        obstacles = new ArrayList<Obstacle>();
        initialObstacles();
    }

    public void initialObstacles() {
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
            getAudioPlayer().cue(0);
            getAudioPlayer().play();
        }
    }

    public void draw() {
        background(0);

        score = (millis() - startTime) / 100;
        text("Score: " + score, 50, 50);

        try {
            calculateFFT();
        } catch (VisualException e) {
            e.printStackTrace();
        }

        calculateFrequencyBands();
        calculateAverageAmplitude();

        player.render();

        ListIterator<Obstacle> itr = obstacles.listIterator();
        while (itr.hasNext()) {

            Area playerBody = player.getCollisionBody();
            Obstacle obstacle = itr.next();

            if (obstacle.didCollideWithPlayer(playerBody)) {
                println("COLLISSION - - -- - - -- - - - - -!!!!");
                startTime = millis();
                score = 0;
            } else {
                println("NO COLLISSION!!!!");

            }

            // Rectangle2D rect = playerBody.getBounds2D();
            // fill(255, 255, 255);
            // rect((float) rect.getX(), (float) rect.getY(), (float) rect.getWidth(),
            // (float) rect.getHeight());

            // Area obstacleBody = obstacle.getCollisionBody();

            // Rectangle2D rect2 = obstacleBody.getBounds2D();
            // fill(0, 255, 255);
            // rect((float) rect2.getX(), (float) rect2.getY(), (float) rect2.getWidth(),
            // (float) rect2.getHeight());

            // Area overlapArea = (Area) playerBody.clone();
            // overlapArea.intersect(obstacleBody);

            // if (!overlapArea.isEmpty()) {
            // println("COLLISSION - - -- - - -- - - - - -!!!!");
            // startTime = millis();
            // score = 0;
            // } else {
            // println("NO COLLISSION!!!!");
            // }

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

            // Rectangle2D rect3 = overlapArea.getBounds2D();
            // fill(255, 0, 0);
            // rect((float) rect3.getX(), (float) rect3.getY(), (float) rect3.getWidth(),
            // (float) rect3.getHeight());
            // fill(200, 100, 200);

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

        speed = getAmplitude() * 50;

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
            previousBeat = isBeat;
        }

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
