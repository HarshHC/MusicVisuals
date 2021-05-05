package d18130149;

import processing.core.PApplet;

public class SideBarsObstacle extends Obstacle {
    public String placement;
    int l, r;
    float size;
    boolean oneSided;

    public SideBarsObstacle(HarshsVisual hv, String placement, int left, int right) {
        super(hv);
        this.placement = placement;
        this.oneSided = left != right;
        this.l = left;
        this.r = right;
        pos.y = -400;
        colorVal = hv.random(0, 255);
    }

    public void show(int left, int right) {
        hv.noStroke();
        // float size = oneSided ? 1.2f : 0.5f;
        float gap = hv.width / (float) hv.getBands().length;

        for (int i = 2; i < hv.getBands().length - 1; i++) {
            float bandValue = hv.getSmoothedBands()[i];

            if (left == right) {
                bandValue = PApplet.map(bandValue, 0, 450, 0, 280);
                PApplet.println("YESS");
            } else {
                PApplet.println("NO");
                bandValue = PApplet.map(bandValue, 0, 400, 0, 480);

            }

            if (placement == Constants.RIGHT) {
                hv.fill(PApplet.map(i, 0, hv.getBands().length, 255, 0), colorVal, 255);
                hv.rect(hv.width, pos.y + gap * i, -bandValue * (oneSided ? 1.2f : 0.6f), gap, 5);
            } else if (placement == Constants.LEFT) {
                hv.fill(PApplet.map(i, 0, hv.getBands().length, 255, 0), colorVal, 255);
                hv.rect(0, pos.y + gap * i, bandValue * (oneSided ? 1.5f : 0.8f), gap, 5);
            }

        }
    }

    public void generateAtRandomEdge() {
        pos.y = 0;
    }

    @Override
    public void getCollisionBody() {

    }

}
