package d18130149;

import processing.core.PApplet;

public class SquareObstacle extends Obstacle {

    private float rotation, size;

    public SquareObstacle(HarshsVisual hv) {
        super(hv);
        rotation = 0;
        size = hv.random(40, 70);
        super.generateAtRandomLocation();
    }

    public void show() {

        hv.pushMatrix();

        hv.translate(x, y);
        float rad = PApplet.radians(rotation);
        hv.rotate(rad);
        hv.rect(0, 0, size, size);

        hv.popMatrix();

    }

    public void rotate(float speed) {
        rotation += speed;
    }

}
