package general.Ghosts;/*
 * Created by L1ND3 on 06.04.2017. 
 */


import general.PlayerState;
import server.ServerMazeMap;

import java.io.Serializable;
import java.util.Map;

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
    public void calculateNewPos(double timeDelta, ServerMazeMap map, Map<Integer, PlayerState> playerStates) {
        float playerCoordX = playerStates.get(0).getX();
        float playerCoordY = playerStates.get(0).getY();
        String[][] smap = map.getMap();
        if (playerCoordX != 0){
            if (playerCoordX-x < 0) {
                x += -timeDelta * speed;
            }
            if (playerCoordX-x > 0){
                x += timeDelta*speed;
            }
        }
        if (playerCoordY != 0) {
            if (playerCoordY - y < 0) {
                y += -timeDelta * speed;
            }
            if (playerCoordY - y > 0) {
                y += timeDelta * speed;
            }
        }
    }
}
