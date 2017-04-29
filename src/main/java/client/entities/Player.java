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
    private static boolean texturesLoaded = false;
    private static Image pacmanRightTexture;
    private static Image pacmanLeftTexture;
    private static Image pacmanUpTexture;
    private static Image pacmanDownTexture;

    public Player(PlayerState state) {
        this.state = state;
        if(!texturesLoaded){
            try {
                pacmanRightTexture = new Image("resources/pacmanRightTexture.png");
                pacmanLeftTexture = new Image("resources/pacmanLeftTexture.png");
                pacmanUpTexture = new Image("resources/pacmanUpTexture.png");
                pacmanDownTexture = new Image("resources/pacmanDownTexture.png");
            } catch (SlickException e) {
                throw new RuntimeException(e);
            }
            texturesLoaded = true;
        }
    }

    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.white);
        double dir = state.getMovementDir();
        float x = (float) state.getLoc().getX();
        float y = (float) state.getLoc().getY();
        Image curTexture;
        if (Math.cos(dir) > 0 && Math.abs(Math.cos(dir)) > Math.abs(Math.sin(dir))) {
            curTexture = pacmanRightTexture;
        } else if (Math.cos(dir) < 0 && Math.abs(Math.cos(dir)) > Math.abs(Math.sin(dir))) {
                curTexture = pacmanLeftTexture;
        } else if (Math.sin(dir) > 0 && Math.abs(Math.cos(dir)) < Math.abs(Math.sin(dir))) {
            curTexture = pacmanUpTexture;
        } else { // if (Math.sin(dir) < 0 && Math.abs(Math.cos(dir)) < Math.abs(Math.sin(dir))) {
            curTexture = pacmanDownTexture;
        }
        g.texture(new Rectangle(x-xSize/2, y-ySize/2, xSize, ySize), curTexture, 1, 1, true);
    }
}
