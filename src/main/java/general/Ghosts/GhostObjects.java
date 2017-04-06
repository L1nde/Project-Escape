package general.Ghosts;/*
 * Created by L1ND3 on 06.04.2017. 
 */

import general.PlayerState;
import server.ServerMazeMap;

import java.util.Map;

public interface GhostObjects {

    void calculateNewPos(double timeDelta, ServerMazeMap map, Map<Integer, PlayerState> playerStates);
    float getX();
    float getY();
}
