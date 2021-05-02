package d18130149;

import processing.core.PApplet;

public class CircleObstacle extends Obstacle {

    private float size;
    private boolean isIncreasing = true;

    public CircleObstacle(HarshsVisual hv) {
        super(hv);
        size = hv.random(50, 90);
        colorVal = hv.random(0, 255);
        super.generateAtRandomLocation();
    }

    public void show() {
        hv.fill(150, colorVal, 255);
        hv.ellipse(x, y, size, size);
    }

    public void scale(float value) {
        if (size > 120) {
            isIncreasing = false;
        } else if (size < 50) {
            isIncreasing = true;
        }

        if (isIncreasing) {
            size += PApplet.map(value, 0, 20, 2, 5);
        } else {
            size -= PApplet.map(value, 0, 20, 2, 5);
        }
    }

}
