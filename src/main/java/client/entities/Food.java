package client.entities;

import client.MapObjects;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by Meelis Perli on 4/5/2017.
 */
public class Food implements MapObjects{

    private final int[] cords;
    private final int radius = 5;

    public Food(int[] cords) {
        this.cords = cords;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        g.setColor(Color.blue);
        g.fillOval(cords[0] + 10 - radius/2, cords[1] + 10 - radius/2, radius, radius);
    }
}
