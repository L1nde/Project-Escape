package client.entities;

import general.GhostState;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;

/**
 * Created by Meelis Perli on 4/24/2017.
 */
public class GhostRusher {

    private float size = 18;
    private final Image ghostTexture;
    private final Image chargeTexture;
    private GhostState state;

    public GhostRusher(GhostState ghost) {
        this.state = ghost;
        try {
            ghostTexture = new Image("src/main/resources/textures/ghostRusherTexture.png");
            chargeTexture = new Image("src/main/resources/textures/ghostRusherTexture1.png");
        } catch (SlickException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(GameContainer container, Graphics g){
        g.setColor(Color.white);
        float x = (float) (state.getLoc().getX() * 20);
        float y = (float) (state.getLoc().getY() * 20);
        if(!state.isSpecialActive()) {
            g.texture(new Rectangle(x-size/2, y-size/2, size, size), ghostTexture, 1, 1, true);
        } else {
            g.texture(new Rectangle(x-size/2, y-size/2, size, size), chargeTexture, 1, 1, true);
        }

    }
}
