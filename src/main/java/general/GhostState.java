package general;

import java.io.Serializable;

public class GhostState implements Serializable{
    private final Point loc;
    private final String type;
    private final boolean specialActive;

    public GhostState(Point loc, String type, boolean specialActive) {
        this.loc = loc;
        this.type = type;
        this.specialActive = specialActive;
    }

    public Point getLoc() {
        return loc;
    }

    public String getType() {
        return type;
    }

    public boolean isSpecialActive() {
        return specialActive;
    }
}
