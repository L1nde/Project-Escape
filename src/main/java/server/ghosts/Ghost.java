package server.ghosts;/*
 * Created by L1ND3 on 06.04.2017. 
 */

import general.GhostType;
import general.Point;

public interface Ghost {

    void calculateNewPos(double timeDelta);
    Point getLoc();
    double getSideLen();
    general.GhostState getAsState();
    GhostType getGhostType();
}
