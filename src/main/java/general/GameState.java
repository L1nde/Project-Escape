package general;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class GameState implements Serializable, Comparable<GameState>{
    private final Map<Integer, PlayerState> playerStates;
    private final Map<Integer, GhostState> ghosts;
    private final List<MapUpdate> mapUpdates;
    private final int tick;
    private final long seconds;
    private final double timePerTick;

    public GameState(Map<Integer, PlayerState> playerStates, Map<Integer, GhostState> ghosts,
                     List<MapUpdate> mapUpdates, int tick, long time, double timePerTick) {
        this.playerStates = playerStates;
        this.ghosts = ghosts;
        this.mapUpdates = mapUpdates;
        this.tick = tick;
        this.seconds = time;
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

    public long getTime(){
        return seconds;
    }

    @Override
    public int compareTo(GameState o) {
        return Integer.compare(tick, o.tick);
    }
}
