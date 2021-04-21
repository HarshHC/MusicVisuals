package ie.tudublin;

import d18130149.HarshsVisual;
// import example.CubeVisual;
// import example.MyVisual;
// import example.RotatingAudioBands;

public class Main {

	public void startUI() {
		String[] a = { "MAIN" };
		processing.core.PApplet.runSketch(a, new HarshsVisual());
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.startUI();
	}
}