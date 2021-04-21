package d18130149;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import ie.tudublin.*;

public class HarshsVisual extends Visual {

    Player p;
    int score, startTime;
    double speed;
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
        speed = 0.1;

        obstacles = new ArrayList<Obstacle>();
        for (int i = 0; i < 3; i++) {
            float gap = width / (float) getBands().length;
            Obstacle leftO = new Obstacle(this, Constants.LEFT, -(i * (gap * 6)));
            Obstacle rightO = new Obstacle(this, Constants.RIGHT, -(i * (gap * 6)));
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
            Obstacle ob = itr.next();

            ob.render();
            ob.move(speed);

            if (ob.isOffScreen()) {
                itr.remove();
                float gap = width / (float) getBands().length;
                if (ob.placement == Constants.LEFT) {
                    Obstacle o = new Obstacle(this, Constants.LEFT, -(gap * 6));
                    itr.add(o);
                } else if (ob.placement == Constants.RIGHT) {
                    Obstacle o = new Obstacle(this, Constants.RIGHT, -(gap * 6));
                    itr.add(o);
                }
            }
        }

        speed = getAmplitude() * 50;

    }
}
