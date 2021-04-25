package d18130149;

import processing.core.PApplet;

public class SideBarsObstacle extends Obstacle {
    public String placement;

    public SideBarsObstacle(HarshsVisual hv, String placement) {
        super(hv);
        this.placement = placement;
    }

    public void show() {
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

    public void generateAtRandomEdge() {
        y = 10;
    }

    // public boolean isOffScreen() {
    // return y >= hv.height - hv.width / (float) hv.getBands().length;

    // }

}
