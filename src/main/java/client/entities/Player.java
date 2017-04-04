package client.entities;

import general.PlayerState;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;


/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class Player{
    private PlayerState state;
    private int xSize = 18;
    private int ySize = 18;

    public Player(PlayerState state) {
        this.state = state;
    }

    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.yellow);
        g.fillArc(Math.round(state.getX()), Math.round(state.getY()), xSize, ySize, 0, 300);
    }
}
