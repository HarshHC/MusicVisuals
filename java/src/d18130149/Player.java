package d18130149;

import processing.core.*;

public class Player {

    HarshsVisual hv;

    public Player(HarshsVisual hv) {
        this.hv = hv;
    }

    public void render() {
        float playerY = 1000;

        // if (hv.mouseY > (hv.height * 0.8)) {
        // playerY = hv.mouseY;
        // } else {
        playerY = PApplet.map((float) hv.mouseY, (float) (hv.height * 0.2), (float) (hv.height * 0.75),
                (float) (hv.height * 0.8), (float) (hv.height * 0.9));

        // }

        hv.ellipse(hv.mouseX, playerY, 50, 50);
        // mv.colorMode(PApplet.HSB);
        // for (int i = 0; i < mv.getAudioBuffer().size(); i++) {
        // mv.stroke(PApplet.map(i, 0, mv.getAudioBuffer().size(), 0, 255), 255, 255);

        // mv.line(i, cy, i, cy + cy * mv.getAudioBuffer().get(i));
        // }
    }
}
