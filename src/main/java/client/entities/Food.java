package client.entities;

import client.MapObjects;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by Meelis Perli on 4/5/2017.
 */
public class Food implements MapObjects{

    private final int[] cords;
    private final int radius = 5;
    private Image foodTexture;

    public Food(int[] cords) {
        this.cords = cords;
        try {
            this.foodTexture = new Image("resources/PizzaTexture.png");
        } catch (SlickException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        g.setColor(Color.white);
//        g.texture(new Rectangle(cords[0]+4, cords[1]+6, 10, 5), foodTexture, 1, 1, true);
        g.texture(new Circle(cords[0] + 10 - radius/2, cords[1] + 10 - radius/2, radius, radius), foodTexture, 1, 1, true);
//        g.fillOval(cords[0] + 10 - radius/2, cords[1] + 10 - radius/2, radius, radius);
    }
}
