package client.entities;

import client.MapObjects;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by Meelis on 30/03/2017.
 */
public class Wall implements MapObjects {
    private final int[] cords;
    private final int side = 20;

    public Wall(int[] cords) {
        this.cords = cords;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(cords[0], cords[1], side, side);
    }
}
