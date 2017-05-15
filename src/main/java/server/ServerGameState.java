package server;

import general.*;
import server.ghosts.Ghost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class ServerGameState implements Comparable<ServerGameState>{
    final private Map<Integer, Player> players;
    private final Map<Integer, Ghost> ghosts;
    private List<MapUpdate> mapUpdates;
    private int tick;
    final private double timePerTick;
    private long startMillisecond;
    private long milliseconds = 0;
    private final ServerMazeMap map;

    public ServerGameState(int tick, double timePerTick, ServerMazeMap map) {
        this.players = new HashMap<>();
        this.ghosts = new HashMap<>();
        this.tick = tick;
        this.timePerTick = timePerTick;
        mapUpdates = map.getAsUpdates();
        this.map = map;
        startMillisecond = System.currentTimeMillis();
    }

    public int getTick() {
        return tick;
    }

    public void setInputs(ConcurrentMap<Integer, PlayerInputState> lastInputs) {
        for(Map.Entry<Integer, Player> entry : players.entrySet()){
            PlayerInputState newInput = lastInputs.getOrDefault(entry.getKey(), new PlayerInputState());
            entry.getValue().setInput(newInput);
        }
    }

    public void addPlayer(int id, Player cur){
        players.put(id, cur);
    }

    public void addGhost(int id, Ghost ghost){
        ghosts.put(id, ghost);
    }

    public void nextState(int targetTick){
        if(targetTick > tick){
            mapUpdates.clear();
            for(Player cur : players.values()){
                cur.calculateNewPos((targetTick-tick)*timePerTick);
            }
            for(Ghost cur : ghosts.values()){
                cur.calculateNewPos((targetTick-tick)*timePerTick);
            }
            for(Player cur : players.values()){
                mapUpdates.addAll(cur.getMapUpdates());
            }
            for(MapUpdate cur : mapUpdates){
                map.apply(cur);
            }
            for(Player cur : players.values()){
                cur.checkEntityCollisions(this);
            }
            tick = targetTick;
            milliseconds = System.currentTimeMillis() - startMillisecond;
        }
    }

    public List<Ghost> getCollidingGhosts(Point loc, double rectRange){
        List<Ghost> res = new ArrayList<>();
        for(Ghost cur : ghosts.values()){
            double xDiff = Math.abs(cur.getLoc().getX() - loc.getX());
            double yDiff = Math.abs(cur.getLoc().getY() - loc.getY());
            double distLim = rectRange + cur.getSideLen()/2;
            if(xDiff < distLim && yDiff < distLim){
                res.add(cur);
            }
        }
        return(res);
    }

    public Point getClosestPlayerLoc(Point start){
        Point res = null;
        for(Player cur : players.values()){
            if(cur.isAlive() && (res == null || start.distance(cur.getLoc()) < start.distance(res))){
                res = cur.getLoc();
            }
        }
        return res;
    }

    public List<Point> getPlayerLocations() {
        List<Point> playerLocations = new ArrayList<>();
        for (Player player : players.values()) {
            if(player.isAlive()) {
                playerLocations.add(player.getLoc());
            }
        }
        return playerLocations;
    }

    @Override
    public int compareTo(ServerGameState o) {
        return Integer.compare(tick, o.tick);
    }

    public GameState toTransmitable(){
        Map<Integer, GhostState> ghostsTransmit = new HashMap<>();
        for (Map.Entry<Integer, Ghost> entry : ghosts.entrySet()) {
            ghostsTransmit.put(entry.getKey(), entry.getValue().getAsState());
        }
        Map<Integer, PlayerState> playersTransmit = new HashMap<>();
        for (Map.Entry<Integer, Player> entry : players.entrySet()) {
            playersTransmit.put(entry.getKey(), entry.getValue().getAsState());
        }
        return new GameState(playersTransmit, ghostsTransmit,
                new ArrayList<MapUpdate>(mapUpdates), tick, milliseconds, timePerTick);
    }
}
