package d18130149;

import processing.core.PApplet;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.AffineTransform;

public class SquareObstacle extends Obstacle {

    private float rotation, size;

    public SquareObstacle(HarshsVisual hv) {
        super(hv);
        rotation = 0;
        size = hv.random(50, 90);
        colorVal = hv.random(0, 255);

        super.generateAtRandomLocation();
    }

    public void show() {

        hv.pushMatrix();

        hv.translate(pos.x, pos.y);
        float rad = PApplet.radians(rotation);
        hv.rotate(rad);

        hv.fill(150, colorVal, 255);
        hv.rect(0, 0, size, size, 10);

        hv.popMatrix();

    }

    public void rotate(float speed) {
        rotation += speed;
        rotation += PApplet.map(speed, 0, 20, 2, 10);
    }

    @Override
    public void getCollisionBody() {
        RoundRectangle2D.Float body = new RoundRectangle2D.Float(pos.x, pos.y, size, size, 10, 10);
        AffineTransform transform = new AffineTransform();
        transform.rotate(rotation, pos.x, pos.y);

    }

}
