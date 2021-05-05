package d18130149;

import processing.core.*;
import java.awt.geom.Area;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

public class Player implements GameObject {

    private HarshsVisual hv;
    private PVector pos, playAreaCenter;
    private float rotation;

    public Player(HarshsVisual hv) {
        this.hv = hv;
        pos = new PVector(hv.mouseX, 1000);
        playAreaCenter = new PVector(hv.width / 2, 1000);
    }

    public void render() {
        pos.x = hv.mouseX;
        pos.y = PApplet.map((float) hv.mouseY, (float) (hv.height * 0.2), (float) (hv.height * 0.75),
                (float) (hv.height * 0.8), (float) (hv.height * 0.9));

        hv.pushMatrix();
        hv.translate(pos.x, pos.y);

        float angle = PVector.angleBetween(pos, playAreaCenter);

        if (hv.mouseX < hv.width / 2) {
            angle *= -1;
        }

        float rad = PApplet.radians(angle * 30);
        hv.rotate(rad);
        rotation = rad;

        hv.triangle(0, -50, -25, 0, 25, 0);

        hv.popMatrix();

    }

    @Override
    public Area getCollisionBody() {
        Polygon triangleBody = new Polygon();
        triangleBody.addPoint(Math.round(pos.x + 40), Math.round(pos.y - 50));
        triangleBody.addPoint(Math.round(pos.x + 10), Math.round(pos.y + 25));
        triangleBody.addPoint(Math.round(pos.x + 60), Math.round(pos.y + 25));

        Area area = new Area(triangleBody);
        return area;
    }
}
