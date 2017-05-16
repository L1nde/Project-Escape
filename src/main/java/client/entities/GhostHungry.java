package client.entities;

import general.GhostState;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;

/**
 * Created by Meelis Perli on 5/16/2017.
 */
public class GhostHungry {
    private float size = 18;
    private static boolean texturesLoaded = false;
    private static Image ghostTexture;
    private GhostState state;

    public GhostHungry(GhostState ghost) {
        this.state = ghost;
        if(!texturesLoaded){
            try {
                ghostTexture = new Image("src/main/resources/textures/ghostHungryTexture.png");
            } catch (SlickException e) {
                throw new RuntimeException(e);
            }
            texturesLoaded = true;
        }
    }

    public void render(GameContainer container, Graphics g){
        g.setColor(Color.white);
        float x = (float) (state.getLoc().getX() * 20);
        float y = (float) (state.getLoc().getY() * 20);
        g.texture(new Rectangle(x-size/2, y-size/2, size, size), ghostTexture, 1, 1, true);
    }
}
