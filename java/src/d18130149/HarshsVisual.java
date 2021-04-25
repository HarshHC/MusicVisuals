package d18130149;

import java.util.ArrayList;
import java.util.ListIterator;

import ie.tudublin.*;

public class HarshsVisual extends Visual {

    Player p;
    int score, startTime;
    float speed;
    ArrayList<Obstacle> obstacles;

    public void settings() {
        size(500, 1024);
    }

    public void setup() {
        surface.setResizable(true);
        startMinim();

        // Call loadAudio to load an audio file to process
        loadAudio("java/data/levitate.mp3");
        getAudioPlayer().cue(0);
        getAudioPlayer().play();

        startTime = millis();
        p = new Player(this);
        speed = 0.1f;

        rectMode(CENTER);

        obstacles = new ArrayList<Obstacle>();
        for (int i = 0; i < 1; i++) {
            float gap = width / (float) getBands().length;
            Obstacle leftO = new SideBarsObstacle(this, Constants.LEFT);
            Obstacle rightO = new SideBarsObstacle(this, Constants.RIGHT);
            obstacles.add(leftO);
            obstacles.add(rightO);
        }

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
            // Call this if you want to use FFT data
            calculateFFT();
        } catch (VisualException e) {
            e.printStackTrace();
        }

        // Call this is you want to use frequency bands
        calculateFrequencyBands();

        // Call this is you want to get the average amplitude
        calculateAverageAmplitude();

        p.render();

        ListIterator<Obstacle> itr = obstacles.listIterator();
        while (itr.hasNext()) {
            Obstacle obstacle = itr.next();
            if (obstacle instanceof SideBarsObstacle) {
                SideBarsObstacle ob = (SideBarsObstacle) obstacle;

                ob.show();
                ob.move(speed);

            } else {
                SquareObstacle ob = (SquareObstacle) obstacle;

                ob.show();
                ob.rotate(speed);
                ob.move(speed);

            }

            if (obstacle.isOffScreen()) {
                itr.remove();
                float gap = width / (float) getBands().length;

                SquareObstacle o = new SquareObstacle(this);
                itr.add(o);
                // if (ob.placement == Constants.LEFT) {
                // SideBarsObstacle o = new SideBarsObstacle(this, Constants.LEFT);
                // o.generateAtRandomEdge();
                // itr.add(o);
                // } else if (ob.placement == Constants.RIGHT) {
                // SideBarsObstacle o = new SideBarsObstacle(this, Constants.RIGHT);
                // o.generateAtRandomEdge();
                // itr.add(o);
                // }
            }
        }

        speed = getAmplitude() * 50;

    }
}
