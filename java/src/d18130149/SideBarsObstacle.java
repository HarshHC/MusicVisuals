package d18130149;

import processing.core.PApplet;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

public class SideBarsObstacle extends Obstacle {
    public String placement;
    private int left, right;
    private boolean oneSided;

    public SideBarsObstacle(HarshsVisual hv, String placement) {
        super(hv);
        this.placement = placement;
        this.oneSided = left != right;
        pos.y = -400;
        colorVal = hv.random(0, 255);
    }

    public void show(int left, int right) {

        this.left = left;
        this.right = right;
        hv.noStroke();

        // calculate gap between the bars
        float gap = hv.width / (float) hv.getBands().length;

        // draw each bar based on music bands
        for (int i = 2; i < hv.getBands().length - 1; i++) {
            float bandValue = hv.getSmoothedBands()[i];

            // alter length of the bars to make space for player if they're on both sides
            if (left == right) {
                bandValue = PApplet.map(bandValue, 0, 450, 0, 280);
            } else {
                bandValue = PApplet.map(bandValue, 0, 400, 0, 480);
            }

            // draw the bars based on the placement variable
            if (placement == Constants.RIGHT) {
                hv.fill(PApplet.map(i, 0, hv.getBands().length, 255, 0), colorVal, 255);
                hv.rect(hv.width, pos.y + gap * i, -bandValue * (oneSided ? 1.2f : 0.6f), gap, 5);
            } else if (placement == Constants.LEFT) {
                hv.fill(PApplet.map(i, 0, hv.getBands().length, 255, 0), colorVal, 255);
                hv.rect(0, pos.y + gap * i, bandValue * (oneSided ? 1.5f : 0.8f), gap, 5);
            }
        }
    }

    @Override
    public Area getCollisionBody() {

        // Initialize an empty area
        Area area = new Area();

        // calculate the band gap
        float gap = hv.width / (float) hv.getBands().length;

        // go over each band and add its area to total area
        for (int i = 2; i < hv.getBands().length - 1; i++) {
            float bandValue = hv.getSmoothedBands()[i];

            // alter the band length to make space for player
            if (left == right) {
                bandValue = PApplet.map(bandValue, 0, 450, 0, 280);
            } else {
                bandValue = PApplet.map(bandValue, 0, 400, 0, 480);
            }

            // calculate area based on placement
            if (placement == Constants.RIGHT) {
                // create a round rectangular shape
                RoundRectangle2D.Float rectBody = new RoundRectangle2D.Float(hv.width, pos.y + gap * i,
                        bandValue * (oneSided ? 1.2f : 0.6f) + 100, gap, 5, 5);

                // get area of the shape and add to total area
                Area rectArea = new Area(rectBody);
                area.add(rectArea);

            } else if (placement == Constants.LEFT) {
                // create a round rectangular shape
                RoundRectangle2D.Float rectBody = new RoundRectangle2D.Float(0, pos.y + gap * i,
                        bandValue * (oneSided ? 1.5f : 0.8f) - 100, gap, 5, 5);

                // get area of the shape and add to total area
                Area rectArea = new Area(rectBody);
                area.add(rectArea);
            }
        }

        return area;
    }

}
