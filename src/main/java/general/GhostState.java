package general;

import java.io.Serializable;

public class GhostState implements Serializable{
    private final Point loc;
    private final GhostType type;
    private final boolean specialActive;

    public GhostState(Point loc, GhostType type, boolean specialActive) {
        this.loc = loc;
        this.type = type;
        this.specialActive = specialActive;
    }

    public Point getLoc() {
        return loc;
    }

    public GhostType getType() {
        return type;
    }

    public boolean isSpecialActive() {
        return specialActive;
    }
}
