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
    private Image pacmanRightTexture;
    private Image pacmanLeftTexture;
    private Image pacmanUpTexture;
    private Image pacmanDownTexture;

    public Player(PlayerState state) {
        this.state = state;
        try {
            this.pacmanRightTexture = new Image("resources/pacmanRightTexture.png");
            this.pacmanLeftTexture = new Image("resources/pacmanLeftTexture.png");
            this.pacmanUpTexture = new Image("resources/pacmanUpTexture.png");
            this.pacmanDownTexture = new Image("resources/pacmanDownTexture.png");
        } catch (SlickException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.white);
        switch (state.getDirection()){
            case "right":
                g.texture(new Rectangle(state.getX(), state.getY(), xSize, ySize), pacmanRightTexture, 1, 1, true);
                break;
            case "left":
                g.texture(new Rectangle(state.getX(), state.getY(), xSize, ySize), pacmanLeftTexture, 1, 1, true);
                break;
            case "up":
                g.texture(new Rectangle(state.getX(), state.getY(), xSize, ySize), pacmanUpTexture, 1, 1, true);
                break;
            case "down":
                g.texture(new Rectangle(state.getX(), state.getY(), xSize, ySize), pacmanDownTexture, 1, 1, true);
                break;

        }
    }
}
