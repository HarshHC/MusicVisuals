package d18130149;

import processing.core.PVector;
import java.awt.geom.Area;

public abstract class Obstacle implements GameObject {

    public HarshsVisual hv;
    public PVector pos;
    public float colorVal;
    public boolean didCollideWithPlayer = false;

    public Obstacle(HarshsVisual hv) {
        this.hv = hv;
        pos = new PVector();
    }

    public float getX() {
        return pos.x;
    }

    public void setX(float x) {
        this.pos.x = x;
    }

    public float getY() {
        return pos.y;
    }

    public void setY(float y) {
        this.pos.y = y;
    }

    public void generateAtRandomLocation() {
        pos.y = 10;
        pos.x = hv.random(10.0f, (float) (hv.width));
    }

    public boolean isOffScreen() {
        return pos.y >= hv.height - 10;
    }

    public void move(double speed) {
        pos.y += 1 * speed;
    }

    public boolean didCollideWithPlayer(Area playerBody) {

        if (didCollideWithPlayer) {
            return false;
        } else {
            Area overlapArea = (Area) playerBody.clone();
            overlapArea.intersect(getCollisionBody());

            if (!overlapArea.isEmpty()) {
                didCollideWithPlayer = true;
                return true;
            } else {
                return false;
            }
        }

    }

}
