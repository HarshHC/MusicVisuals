package d18130149;

import java.awt.geom.Area;

public interface GameObject {
    // interface implemented by all game objects - Player and Obstacles
    public Area getCollisionBody();
}
