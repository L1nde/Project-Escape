package client.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * Created by Meelis on 30/03/2017.
 */
public class Wall {
    private final int[] cords;
    private final int side = 20;

    public Wall(int[] cords) {
        this.cords = cords;
    }

    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(cords[0], cords[1], side, side);
    }
}
