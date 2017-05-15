package general;

import java.io.Serializable;

public class GhostState implements Serializable{
    final Point loc;

    public GhostState(Point loc) {
        this.loc = loc;
    }

    public Point getLoc() {
        return loc;
    }
}
