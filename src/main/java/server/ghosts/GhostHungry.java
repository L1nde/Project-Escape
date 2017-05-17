package server.ghosts;

import general.GhostState;
import general.GhostType;
import server.ServerGameState;
import server.ServerMazeMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Meelis Perli on 5/16/2017.
 */
public class GhostHungry extends GhostMoveRandom {
    private ServerGameState gameState;
    private final GhostType type = GhostType.HUNGRY;
    public GhostHungry(double x, double y, double speed, ServerMazeMap map, ServerGameState gameState ) {
        super(x, y, speed, map, gameState);
        this.gameState = gameState;
    }

    @Override
    public GhostState getAsState() {
        return new GhostState(super.getLoc(), type, false);
    }

    @Override
    public GhostType getGhostType() {
        return type;
    }

    @Override
    public void reset(){
        super.reset();
        Map<Integer, Ghost> ghosts = new HashMap<>();
        ghosts.putAll(gameState.getGhosts());
        for (Integer integer : gameState.getGhosts().keySet()) {
            if (integer >= 100000) {
                ghosts.remove(integer);
            }
        }
        gameState.setGhosts(ghosts);

    }
}
