package client.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class Player {
    private float x;
    private float y;
    private int xSize = 20;
    private int ySize = 20;


    public Player(float x, float y) {
        this.x = x;
        this.y = y;

    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String update(GameContainer gc) {
        Input input = gc.getInput();
        if (input.isKeyDown(Input.KEY_UP) || (input.isKeyDown(Input.KEY_W))) {
            return String.valueOf("up");
        }
        if (input.isKeyDown(Input.KEY_DOWN) || (input.isKeyDown(Input.KEY_S))) {
            return String.valueOf("down");
        }
        if (input.isKeyDown(Input.KEY_LEFT) || (input.isKeyDown(Input.KEY_A))) {
            return String.valueOf("left");
        }
        if (input.isKeyDown(Input.KEY_RIGHT) || (input.isKeyDown(Input.KEY_D))) {
            return String.valueOf("right");
        }
        return "nothing";
    }

    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.black);
        g.fillRect(Math.round(x), Math.round(y), xSize, ySize);
    }
}
