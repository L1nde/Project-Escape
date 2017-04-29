package general;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class GameState implements Serializable, Comparable<GameState>{
    final private Map<Integer, PlayerState> playerStates;
    private final Map<Integer, GhostState> ghosts;
    private final List<MapUpdate> mapUpdates;
    private int tick;
    final private double timePerTick;

    public GameState(Map<Integer, PlayerState> playerStates, Map<Integer, GhostState> ghosts,
                     List<MapUpdate> mapUpdates, int tick, double timePerTick) {
        this.playerStates = playerStates;
        this.ghosts = ghosts;
        this.mapUpdates = mapUpdates;
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

    public List<MapUpdate> getMapUpdates() {
        return mapUpdates;
    }

    @Override
    public int compareTo(GameState o) {
        return Integer.compare(tick, o.tick);
    }
}
