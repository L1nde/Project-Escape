package server;

import general.GameState;

import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class ServerSender implements Callable<Void>{
    final private ObjectOutputStream netOut;
    final private BlockingQueue<GameState> myStateQueue;
    private final ServerMazeMap map;

    public ServerSender(BlockingQueue<GameState> myStateQueue, ObjectOutputStream netOut, ServerMazeMap map) {
        this.netOut = netOut;
        this.myStateQueue = myStateQueue;
        this.map = map;
    }

    @Override
    public Void call() throws Exception {
        GameState curMapState = (new ServerGameState(0, 0, map)).toTransmitable();
        netOut.writeObject(curMapState);
        while(true){
            GameState gameState = myStateQueue.take();
            //Without reset values inside map wont get updated on receiving end.
            netOut.reset();
            netOut.writeObject(gameState);
            netOut.flush();
        }
    }
}
