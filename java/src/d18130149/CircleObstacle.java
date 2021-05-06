package d18130149;

import processing.core.PApplet;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class CircleObstacle extends Obstacle {

    private float size;
    private boolean isIncreasing = true;

    // constructor
    public CircleObstacle(HarshsVisual hv) {
        super(hv);
        size = hv.random(50, 90);
        colorVal = hv.random(0, 255);
        super.generateAtRandomLocation();
    }

    public void show() {
        // draw the circle
        hv.fill(150, colorVal, 255);
        hv.ellipse(pos.x, pos.y, size, size);
    }

    public void scale(float value) {
        // transform- scale the circle based on music

        if (size > 120) {
            isIncreasing = false;
        } else if (size < 50) {
            isIncreasing = true;
        }

        // toggle scaling
        if (isIncreasing) {
            size += PApplet.map(value, 0, 20, 2, 5);
        } else {
            size -= PApplet.map(value, 0, 20, 2, 5);
        }
    }

    @Override
    public Area getCollisionBody() {
        // create an ellipse shape based on current transformed(scaled) size
        Ellipse2D.Float body = new Ellipse2D.Float(pos.x, pos.y, size, size);

        // get area of the shape
        Area area = new Area(body);

        return area;
    }

}
