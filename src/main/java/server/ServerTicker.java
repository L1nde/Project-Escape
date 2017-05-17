package server;


import general.GameState;
import general.PlayerInputState;
import general.Point;
import server.ghosts.GhostHungry;
import server.ghosts.GhostLeaper;
import server.ghosts.GhostRusher;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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

    final private double playerDefaultSpeed = 0.05;
    final private double playerDefaultX = 20;
    final private double playerDefaultY = 15;
    final private long  tickDelay = (long)1e9f/60; // in nanoseconds
    final private double timePerTick = 1.f;
    final public static double EPS = 1e-8;
    // Tickrate is 300 ticks/second at the moment.

    public ServerTicker(ServerMazeMap map) {
        //lastInputs is accessed often by ServerReceiver threads so it is Concurrent
        this.lastInputs = new ConcurrentHashMap<>();
        //Synchronization needed in getMyStateQueue
        this.gameStateDistributor = Collections.synchronizedMap(new HashMap<>());
        gameState = new ServerGameState(0, timePerTick, map);
        this.map = map;
        //gameState.addGhost(0, new GhostMoveRandom(10, 7, playerDefaultSpeed, map, gameState));
        //gameState.addGhost(1, new GhostMoveRandom(30, 7, playerDefaultSpeed, map, gameState));
        //gameState.addGhost(2, new GhostMoveRandom(10, 23, playerDefaultSpeed, map, gameState));
        //gameState.addGhost(3, new GhostMoveRandom(30, 23, playerDefaultSpeed, map, gameState));
        gameState.addGhost(4, new GhostRusher(20,20, playerDefaultSpeed, map, gameState));
        gameState.addGhost(5, new GhostLeaper(1,1, playerDefaultSpeed, map, gameState));
        gameState.addGhost(6, new GhostHungry(30,30, playerDefaultSpeed, map, gameState));



    }

    public void addPlayer(int newId){
        synchronized (this){
            lastInputs.putIfAbsent(newId, new PlayerInputState());
            gameStateDistributor.put(newId, new LinkedBlockingQueue<>());
            gameState.addPlayer(newId,
                    new Player(map.findRandomValidPoint(new Point(playerDefaultX, playerDefaultY), 5),
                    playerDefaultSpeed, 3, map));
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

