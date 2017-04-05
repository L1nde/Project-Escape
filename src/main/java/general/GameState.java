package general;

import com.sun.media.jfxmedia.events.PlayerStateEvent;
import server.ServerMazeMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class GameState implements Serializable, Comparable<GameState>{
    final private Map<Integer, PlayerState> playerStates;
    private MapUpdate mapUpdate;
    private int tick;
    final private float timePerTick;

    public GameState(int tick, float timePerTick) {
        this.playerStates = new HashMap<>();
        this.tick = tick;
        this.timePerTick = timePerTick;
    }

    public GameState(GameState other, ServerMazeMap map){
        this(other.tick, other.timePerTick);
        playerStates.putAll(other.playerStates);
        mapUpdate = map.getMapUpdate();
    }

    public int getTick() {
        return tick;
    }

    public Map<Integer, PlayerState> getPlayerStates() {
        return playerStates;
    }

    public MapUpdate getMapUpdate() {
        return mapUpdate;
    }

    public void setInputs(ConcurrentMap<Integer, PlayerInputState> lastInputs) {
        for(Map.Entry<Integer, PlayerState> entry : playerStates.entrySet()){
            PlayerInputState newInput = lastInputs.getOrDefault(entry.getKey(), new PlayerInputState());
            entry.getValue().setInput(newInput);
        }


    }

    public void addPlayer(int id, PlayerState state){
        playerStates.put(id, state);
    }

    public void nextState(int targetTick, ServerMazeMap map){
        if(targetTick > tick){
            for(Map.Entry<Integer, PlayerState> entry : playerStates.entrySet()){
                entry.getValue().calculateNewPos((targetTick - tick)*timePerTick, map);
            }
            tick = targetTick;
        }
    }

    @Override
    public int compareTo(GameState o) {
        return Integer.compare(tick, o.tick);
    }
}
