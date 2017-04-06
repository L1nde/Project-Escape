package client.entities;/*
 * Created by L1ND3 on 06.04.2017. 
 */

import general.Ghosts.GhostObjects;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;


public class Ghost {
    private float x;
    private float y;
    private float size = 18;
    private Image ghostTexture;
    private GhostObjects ghost;

    public Ghost(GhostObjects ghost) {
        this.ghost = ghost;
        try {
            ghostTexture = new Image("resources/ghostTexture.png");
        } catch (SlickException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(GameContainer container, Graphics g){
        g.setColor(Color.white);
        g.texture(new Rectangle(ghost.getX(), ghost.getY(), size, size), ghostTexture, 1, 1, true);
    }
}
