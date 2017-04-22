package client.entities;

import client.MapObjects;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by Meelis on 30/03/2017.
 */
public class Wall implements MapObjects {
    private final int[] cords;
    private final int side = 20;
    private Image wall;

    public Wall(int[] cords) {
        this.cords = cords;
        try {
            this.wall = new Image("src/main/resources/wall.png");
        } catch (SlickException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        g.setColor(Color.white);
        g.texture(new Rectangle(cords[0],cords[1],side,side), wall, 1, 1, true);
    }
}
