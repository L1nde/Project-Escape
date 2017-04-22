package general;

import server.ServerMazeMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GameState implements Serializable, Comparable<GameState>{
    final private Map<Integer, PlayerState> playerStates;
    private final Map<Integer, GhostState> ghosts;
    private MapUpdate mapUpdate;
    private int tick;
    final private float timePerTick;

    public GameState(Map<Integer, PlayerState> playerStates, Map<Integer, GhostState> ghosts,
                     MapUpdate mapUpdate, int tick, float timePerTick) {
        this.playerStates = playerStates;
        this.ghosts = ghosts;
        this.mapUpdate = mapUpdate;
        this.tick = tick;
        this.timePerTick = timePerTick;
    }

    public int getTick() {
        return tick;
    }

    public Map<Integer, PlayerState> getPlayerStates() {
        return playerStates;
    }

    public Map<Integer, GhostState> getGhostsStates() {
        return ghosts;
    }

    public MapUpdate getMapUpdate() {
        return mapUpdate;
    }

    @Override
    public int compareTo(GameState o) {
        return Integer.compare(tick, o.tick);
    }
}
