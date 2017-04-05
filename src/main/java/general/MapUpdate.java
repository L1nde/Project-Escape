package general;

import java.io.Serializable;

/**
 * Created by Meelis Perli on 4/5/2017.
 */
public class MapUpdate implements Serializable {
    final private int x;
    final private int y;
    private String newTile;

    public MapUpdate(int x, int y, String newTile) {
        this.x = x;
        this.y = y;
        this.newTile = newTile;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getNewTile() {
        return newTile;
    }
}
