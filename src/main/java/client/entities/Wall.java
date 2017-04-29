package client.entities;

import client.MapObjects;
import general.Point;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by Meelis on 30/03/2017.
 */
public class Wall implements MapObjects {
    private final Point loc;
    private final int side = 20;
    private static boolean texturesLoaded = false;
    private static Image wall;

    public Wall(Point loc) {
        this.loc = loc;
        if(!texturesLoaded){
            try {
                this.wall = new Image("resources/wall.png");
            } catch (SlickException e) {
                throw new RuntimeException(e);
            }
            texturesLoaded = true;
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        g.setColor(Color.white);
        g.texture(new Rectangle((float)loc.getX()-side/2, (float)loc.getY()-side/2, side, side), wall, 1, 1, true);
    }
}
