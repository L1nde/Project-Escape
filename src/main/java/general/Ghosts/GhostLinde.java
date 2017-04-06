package general.Ghosts;/*
 * Created by L1ND3 on 06.04.2017. 
 */


import server.ServerMazeMap;

import java.io.Serializable;

public class GhostLinde implements GhostObjects, Serializable {
    private float x;
    private float y;
    private float speed;


    public GhostLinde(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }



    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void calculateNewPos(double timeDelta, ServerMazeMap map) {

    }
}
