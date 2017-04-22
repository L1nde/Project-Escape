package general;

import java.io.Serializable;

public class GhostState implements Serializable{
    Point location;

    public GhostState(Point location) {
        this.location = location;
    }

    public float getX() {
        return location.getX();
    }

    public float getY() {
        return location.getY();
    }
}
