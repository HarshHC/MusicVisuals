package d18130149;

public class Obstacle {

    public HarshsVisual hv;
    public float x, y, colorVal;

    public Obstacle(HarshsVisual hv) {
        this.hv = hv;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void generateAtRandomLocation() {
        y = 10;
        x = hv.random(10.0f, (float) (hv.width));
    }

    public boolean isOffScreen() {
        return y >= hv.height - 10;
    }

    public void move(double speed) {
        y += 1 * speed;
    }

}
