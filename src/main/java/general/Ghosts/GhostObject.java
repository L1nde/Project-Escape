package general.Ghosts;/*
 * Created by L1ND3 on 06.04.2017. 
 */

import java.io.Serializable;

public class GhostObject implements Serializable {
    private float x;
    private float y;
    private boolean moving = false;
    private float speed;
    private int count;

    public GhostObject(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
