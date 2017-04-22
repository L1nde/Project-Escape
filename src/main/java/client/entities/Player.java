package client.entities;

import general.PlayerState;
import org.newdawn.slick.*;
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
            this.pacmanRightTexture = new Image("src/main/resources/pacmanRightTexture.png");
            this.pacmanLeftTexture = new Image("src/main/resources/pacmanLeftTexture.png");
            this.pacmanUpTexture = new Image("src/main/resources/pacmanUpTexture.png");
            this.pacmanDownTexture = new Image("src/main/resources/pacmanDownTexture.png");
        } catch (SlickException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.white);
        double dx = state.getdX();
        double dy = state.getdY();
        if (Math.round(dx) == 1) {
            g.texture(new Rectangle(state.getX(), state.getY(), xSize, ySize), pacmanRightTexture, 1, 1, true);
        }
        else if (Math.round(dx) == -1) {
            g.texture(new Rectangle(state.getX(), state.getY(), xSize, ySize), pacmanLeftTexture, 1, 1, true);
        }
        else if (Math.round(dy) == -1) {
            g.texture(new Rectangle(state.getX(), state.getY(), xSize, ySize), pacmanUpTexture, 1, 1, true);
        }
        else if (Math.round(dy) == 1){
            g.texture(new Rectangle(state.getX(), state.getY(), xSize, ySize), pacmanDownTexture, 1, 1, true);
        }
        else{
            g.texture(new Rectangle(state.getX(), state.getY(), xSize, ySize), pacmanRightTexture, 1, 1, true);

        }
    }
}
