package d18130149;

import processing.core.PVector;
import java.awt.geom.Area;

public abstract class Obstacle implements GameObject {
    // Abstract class for all game obstacles

    public HarshsVisual hv;
    public PVector pos;
    public float colorVal;
    public boolean didCollideWithPlayer = false;

    // constructor
    public Obstacle(HarshsVisual hv) {
        this.hv = hv;
        pos = new PVector();
    }

    // getters and setters
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

    // check if the obstacle collided with the player
    public boolean didCollideWithPlayer(Area playerBody) {

        if (didCollideWithPlayer) {
            // draw the HIT! text if collision occured
            hv.textSize(30);
            hv.text("HIT!", hv.width * 0.8f, 50);
            return false; // false so it does not count collision again
        } else {
            // calculate the overlap between the two areas
            Area overlapArea = (Area) playerBody.clone();
            overlapArea.intersect(getCollisionBody());

            // if overlap area is not empty then collision occured
            if (!overlapArea.isEmpty()) {
                didCollideWithPlayer = true;
                return true;
            } else {
                return false;
            }
        }

    }

}
