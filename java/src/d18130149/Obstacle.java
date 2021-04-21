package d18130149;

import processing.core.*;

public class Obstacle {

    private HarshsVisual hv;
    public String placement;
    private float y = 0;

    public Obstacle(HarshsVisual hv, String placement, float startY) {
        this.hv = hv;
        this.placement = placement;
        this.y = startY;
    }

    public void render() {
        float gap = hv.width / (float) hv.getBands().length;
        hv.noStroke();
        for (int i = 2; i < hv.getBands().length - 1; i++) {
            if (placement == Constants.RIGHT) {
                hv.fill(PApplet.map(i, 0, hv.getBands().length, 255, 0), 255, 255);
                hv.rect(hv.width, y + gap * i, -hv.getSmoothedBands()[i] * 0.5f, gap);
            } else if (placement == Constants.LEFT) {
                hv.fill(PApplet.map(i, 0, hv.getBands().length, 255, 0), 255, 255);
                hv.rect(0, y + gap * i, hv.getSmoothedBands()[i] * 0.4f, gap);
            }

        }
    }

    public void move(double speed) {
        y += 1 * speed;
    }

    public boolean isOffScreen() {
        return y >= hv.height - hv.width / (float) hv.getBands().length;

    }
}
