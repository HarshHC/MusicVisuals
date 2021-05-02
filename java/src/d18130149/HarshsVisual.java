package d18130149;

import java.util.ArrayList;
import java.util.ListIterator;

import ie.tudublin.*;

public class HarshsVisual extends Visual {

    Player p;
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
        p = new Player(this);
        speed = 0.1f;

        rectMode(CENTER);

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

        Obstacle leftO = new SideBarsObstacle(this, Constants.LEFT, leftObstacles != rightObstacles);
        Obstacle rightO = new SideBarsObstacle(this, Constants.RIGHT, leftObstacles != rightObstacles);
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

        p.render();

        ListIterator<Obstacle> itr = obstacles.listIterator();
        while (itr.hasNext()) {
            Obstacle obstacle = itr.next();
            if (obstacle instanceof SideBarsObstacle) {
                SideBarsObstacle ob = (SideBarsObstacle) obstacle;

                ob.show();
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

        // println(min + " -> " + avg / bands.length + " <- " + max + " = " + isBeat);

        if (isBeat != previousBeat) {
            if (obstacles.size() < 10) {
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
                SideBarsObstacle o = new SideBarsObstacle(this, Constants.RIGHT, leftObstacles != rightObstacles);
                rightObstacles++;
                obstacles.add(o);
            } else if (randomSide == 1) {
                SideBarsObstacle o = new SideBarsObstacle(this, Constants.LEFT, leftObstacles != rightObstacles);
                leftObstacles++;
                obstacles.add(o);
            }
        }
    }
}
