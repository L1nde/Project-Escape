package server;

import general.GameState;
import general.Point;
import server.Ghosts.GhostLinde;
import general.PlayerInputState;
import server.Ghosts.GhostMoveRandom;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class ServerTicker implements Runnable {
    final private ConcurrentMap<Integer, PlayerInputState> lastInputs;
    final private Map<Integer, BlockingQueue<GameState> > gameStateDistributor;

    private final ServerMazeMap map;
    private final ServerGameState gameState;

    final private double playerDefaultSpeed = 1;
    final private double playerDefaultX = 100;
    final private double playerDefaultY = 100;
    final private long  tickDelay = (long)1e9f/300; // in nanoseconds
    final private double timePerTick = 0.2f;
    final public static double EPS = 1e-8f;
    // Tickrate is 300 ticks/second at the moment.

    public ServerTicker(ServerMazeMap map) {
        //lastInputs is accessed often by ServerReceiver threads so it is Concurrent
        this.lastInputs = new ConcurrentHashMap<>();
        //Synchronization needed in getMyStateQueue
        this.gameStateDistributor = Collections.synchronizedMap(new HashMap<>());
        gameState = new ServerGameState(0, timePerTick, map);
        this.map = map;
    }

    public void addPlayer(int newId){
        synchronized (this){
            lastInputs.putIfAbsent(newId, new PlayerInputState());
            gameStateDistributor.put(newId, new LinkedBlockingQueue<>());
            gameState.addPlayer(newId, new Player(new Point(playerDefaultX, playerDefaultY), playerDefaultSpeed, map));
            for(int i = 0; i<10; ++i){
                gameState.addGhost(11*newId+i, new GhostMoveRandom(playerDefaultX, playerDefaultY, playerDefaultSpeed, map));
            }
            gameState.addGhost(11*newId+10, new GhostLinde(playerDefaultX, playerDefaultY, playerDefaultSpeed, map));
        }
    }

    public ConcurrentMap<Integer, PlayerInputState> getLastInputs() {
        return lastInputs;
    }

    public BlockingQueue<GameState> getMyStateQueue(int id){
        return gameStateDistributor.get(id);
    }

    @Override
    public void run() {
        long nextTickStart = System.currentTimeMillis();
        int tick = 0;
        while(true){
            synchronized (this){
                gameState.setInputs(lastInputs);
                gameState.nextState(tick + 1);
                ++tick;
                for(Map.Entry<Integer, BlockingQueue<GameState> > entry : gameStateDistributor.entrySet()){
                    entry.getValue().add(gameState.toTransmitable());
                }
            }
            nextTickStart += tickDelay/1e6;
            //while(System.currentTimeMillis() < nextTickStart){}
            //All sleep methods are OS limited(1ms on linux ~10 ms on Windows)
            LockSupport.parkUntil(nextTickStart);
        }
    }
}

