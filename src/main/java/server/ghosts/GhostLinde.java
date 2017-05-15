package server.ghosts;
/*
 * Created by L1ND3 on 06.04.2017. 
 */


import general.GhostState;
import general.Point;
import server.ServerMazeMap;

import java.io.Serializable;

public class GhostLinde implements Ghost, Serializable {
    private double x;
    private double y;
    private double speed;
    private double sideLen = 1;
    private ServerMazeMap map;

    public GhostLinde(double x, double y, double speed, ServerMazeMap map) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.map = map;
    }

    @Override
    public Point getLoc() {
        return new Point(x, y);
    }

    @Override
    public void calculateNewPos(double timeDelta) {

    }
    @Override
    public GhostState getAsState() {
        return new GhostState(new Point(x, y));
    }

    @Override
    public double getSideLen() {
        return sideLen;
    }
}
