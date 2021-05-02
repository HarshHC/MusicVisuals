package d18130149;

import processing.core.PApplet;

public class SideBarsObstacle extends Obstacle {
    public String placement;
    float size;
    boolean oneSided;

    public SideBarsObstacle(HarshsVisual hv, String placement, boolean oneSided) {
        super(hv);
        this.placement = placement;
        this.oneSided = oneSided;
        y = -400;
        colorVal = hv.random(0, 255);
    }

    public void show() {
        hv.noStroke();
        // float size = oneSided ? 1.2f : 0.5f;
        float gap = hv.width / (float) hv.getBands().length;
        for (int i = 2; i < hv.getBands().length - 1; i++) {
            if (placement == Constants.RIGHT) {
                hv.fill(PApplet.map(i, 0, hv.getBands().length, 255, 0), colorVal, 255);
                hv.rect(hv.width, y + gap * i, -hv.getSmoothedBands()[i] * (oneSided ? 1.2f : 0.6f), gap, 5);
            } else if (placement == Constants.LEFT) {
                hv.fill(PApplet.map(i, 0, hv.getBands().length, 255, 0), colorVal, 255);
                hv.rect(0, y + gap * i, hv.getSmoothedBands()[i] * (oneSided ? 1.5f : 0.8f), gap, 5);
            }

        }
    }

    public void generateAtRandomEdge() {
        y = 0;
    }

    // public boolean isOffScreen() {
    // return y >= hv.height - hv.width / (float) hv.getBands().length;

    // }

}
