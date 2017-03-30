package server;

import general.GameState;
import general.PlayerInputState;
import general.PlayerState;

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

    private GameState gameState;
    final private float playerDefaultSpeed = 1;
    final private float playerDefaultX = 100;
    final private float playerDefaultY = 100;
    final private long  tickDelay = (long)1e9f/60; // in nanoseconds
    final private float timePerTick = 1.0f;
    // Tickrate is 60 ticks/second at the moment.

    public ServerTicker() {
        //lastInputs is accessed often by ServerReceiver threads so it is Concurrent
        this.lastInputs = new ConcurrentHashMap<>();
        //Synchronization needed in getMyStateQueue
        this.gameStateDistributor = Collections.synchronizedMap(new HashMap<>());
        gameState = new GameState(0, timePerTick);
    }

    public void addPlayer(int newId){
        synchronized (this){
            lastInputs.putIfAbsent(newId, new PlayerInputState());
            gameStateDistributor.put(newId, new LinkedBlockingQueue<>());
            gameState.addPlayer(newId, new PlayerState(playerDefaultX, playerDefaultY, playerDefaultSpeed));
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
                gameState.nextState(tick +1);
                ++tick;
                for(Map.Entry<Integer, BlockingQueue<GameState> > entry : gameStateDistributor.entrySet()){
                    entry.getValue().add(new GameState(gameState));
                }
            }
            nextTickStart += tickDelay/1e6;
            //while(System.currentTimeMillis() < nextTickStart){}
            //All sleep methods are OS limited(1ms on linux ~10 ms on Windows)
            LockSupport.parkUntil(nextTickStart);
        }
    }
}

