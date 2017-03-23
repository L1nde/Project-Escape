package client.entities;/*
 * Created by L1ND3 on 20.03.2017. 
 */

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.net.Socket;

public class otherPlayer {
    private float x;
    private float y;
    private float speed;
    private int xSize = 20;
    private int ySize = 20;
    private int id;


    public otherPlayer(float x, float y, float speed, int id) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.id = id;

    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getId(){
        return id;
    }

    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.black);
        g.fillRect(Math.round(x), Math.round(y), xSize, ySize);
    }
}
