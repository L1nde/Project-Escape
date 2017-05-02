package client.entities;/*
 * Created by L1ND3 on 06.04.2017. 
 */

import general.ghosts.GhostObject;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;


public class Ghost {
    private float size = 18;
    private Image ghostTexture;
    private GhostObject ghost;

    public Ghost(GhostObject ghost) {
        this.ghost = ghost;
        try {
            ghostTexture = new Image("src/main/resources/textures/ghostTexture.png");
        } catch (SlickException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(GameContainer container, Graphics g){
        g.setColor(Color.white);
        g.texture(new Rectangle(ghost.getX(), ghost.getY(), size, size), ghostTexture, 1, 1, true);
    }
}
