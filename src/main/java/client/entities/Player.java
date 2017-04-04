package client.entities;

import general.PlayerState;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;


/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class Player{
    private PlayerState state;
    private int xSize = 18;
    private int ySize = 18;
    private Image pacmanTexture;

    public Player(PlayerState state) {
        this.state = state;
        try {
            this.pacmanTexture = new Image("resources/pacmanTexture.png");
        } catch (SlickException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.white);
        g.texture(new Rectangle(state.getX(), state.getY(), xSize, ySize), pacmanTexture, 1, 1, true);
    }
}
