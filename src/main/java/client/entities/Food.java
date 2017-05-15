package client.entities;

import client.MapObjects;
import general.Point;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by Meelis Perli on 4/5/2017.
 */
public class Food implements MapObjects{

    private final Point loc;
    private final int radius = 5;

    public Food(Point loc) {
        this.loc = loc;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        float x = (float) (loc.getX()*20);
        float y = (float) (loc.getY()*20);
        g.setColor(Color.blue);
        g.fillRect(x - radius/2, y - radius/2, radius, radius);
    }
}
