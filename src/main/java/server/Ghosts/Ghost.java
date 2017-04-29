package server.Ghosts;/*
 * Created by L1ND3 on 06.04.2017. 
 */

import general.Point;

public interface Ghost {

    void calculateNewPos(double timeDelta);
    Point getLoc();
    general.GhostState getAsState();
}
