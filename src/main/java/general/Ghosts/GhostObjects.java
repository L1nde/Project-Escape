package general.Ghosts;/*
 * Created by L1ND3 on 06.04.2017. 
 */

import server.ServerMazeMap;

public interface GhostObjects {

    void calculateNewPos(double timeDelta, ServerMazeMap map);
    float getX();
    float getY();
}
