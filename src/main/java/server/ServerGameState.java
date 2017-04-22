package server;

import general.*;
import server.Ghosts.GhostObjects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class ServerGameState implements Comparable<ServerGameState>{
    final private Map<Integer, PlayerState> playerStates;
    private final Map<Integer, GhostObjects> ghosts;
    private MapUpdate mapUpdate;
    private int tick;
    final private float timePerTick;

    public ServerGameState(int tick, float timePerTick) {
        this.playerStates = new HashMap<>();
        this.ghosts = new HashMap<>();
        this.tick = tick;
        this.timePerTick = timePerTick;
    }

    public ServerGameState(ServerGameState other, ServerMazeMap map){
        this(other.tick, other.timePerTick);
        playerStates.putAll(other.playerStates);
        mapUpdate = map.getMapUpdate();
        ghosts.putAll(other.ghosts);
    }

    public int getTick() {
        return tick;
    }

    public Map<Integer, PlayerState> getPlayerStates() {
        return playerStates;
    }

    public Map<Integer, GhostObjects> getGhosts() {
        return ghosts;
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

    public void addGhost(int id, GhostObjects ghost){
        ghosts.put(id, ghost);
    }

    public void nextState(int targetTick, ServerMazeMap map){
        if(targetTick > tick){
            for(Map.Entry<Integer, PlayerState> entry : playerStates.entrySet()){
                entry.getValue().calculateNewPos((targetTick - tick)*timePerTick, map);
            }
            for (Map.Entry<Integer, GhostObjects> entry : ghosts.entrySet()) {
                entry.getValue().calculateNewPos((targetTick - tick)*timePerTick, map);
            }
            tick = targetTick;
        }
    }

    @Override
    public int compareTo(ServerGameState o) {
        return Integer.compare(tick, o.tick);
    }

    public GameState toTransmitable(){
        Map<Integer, GhostState> ghostsTransmit = new HashMap<>();
        for (Map.Entry<Integer, GhostObjects> entry : ghosts.entrySet()) {
            ghostsTransmit.put(entry.getKey(), entry.getValue().getAsState());
        }
        return new GameState(playerStates, ghostsTransmit,
                mapUpdate, tick, timePerTick);
    }
}
