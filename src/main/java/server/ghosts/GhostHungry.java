package server.ghosts;

import general.GhostState;
import general.GhostType;
import server.ServerGameState;
import server.ServerMazeMap;

/**
 * Created by Meelis Perli on 5/16/2017.
 */
public class GhostHungry extends GhostMoveRandom {

    private final GhostType type = GhostType.HUNGRY;
    public GhostHungry(double x, double y, double speed, ServerMazeMap map, ServerGameState gameState) {
        super(x, y, speed, map, gameState);
    }

    @Override
    public GhostState getAsState() {
        return new GhostState(super.getLoc(), type, false);
    }

    @Override
    public GhostType getGhostType() {
        return type;
    }

}
