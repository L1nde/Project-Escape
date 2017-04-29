package client.entities;/*
 * Created by L1ND3 on 06.04.2017. 
 */

import general.GhostState;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;


public class Ghost {
    private float size = 18;
    private static boolean texturesLoaded = false;
    private static Image ghostTexture;
    private GhostState state;

    public Ghost(GhostState ghost) {
        this.state = ghost;
        if(!texturesLoaded){
            try {
                ghostTexture = new Image("resources/ghostTexture.png");
            } catch (SlickException e) {
                throw new RuntimeException(e);
            }
            texturesLoaded = true;
        }
    }

    public void render(GameContainer container, Graphics g){
        g.setColor(Color.white);
        float x = (float) state.getLoc().getX();
        float y = (float) state.getLoc().getY();
        g.texture(new Rectangle(x-size/2, y-size/2, size, size), ghostTexture, 1, 1, true);
    }
}
