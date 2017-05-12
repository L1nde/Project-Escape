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
    private static boolean texturesLoaded = false;
    private static Image foodTexture;

    public Food(Point loc) {
        this.loc = loc;
        if(!texturesLoaded){
            try {
                this.foodTexture = new Image("resources/PizzaTexture.png");
            } catch (SlickException e) {
                throw new RuntimeException(e);
            }
            texturesLoaded = true;
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        g.setColor(Color.white);
        float x = (float) (loc.getX()*20);
        float y = (float) (loc.getY()*20);
//        g.texture(new Rectangle(cords[0]+4, cords[1]+6, 10, 5), foodTexture, 1, 1, true);
        g.texture(new Circle(x - radius/2, y - radius/2, radius, radius), foodTexture, 1, 1, true);
//        g.fillOval(cords[0] + 10 - radius/2, cords[1] + 10 - radius/2, radius, radius);
    }
}
