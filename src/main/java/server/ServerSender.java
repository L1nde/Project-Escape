package server;

import general.GameState;

import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class ServerSender implements Callable<Void>{
    final private ObjectOutputStream netOut;
    final private BlockingQueue<GameState> myStateQueue;

    public ServerSender(BlockingQueue<GameState> myStateQueue, ObjectOutputStream netOut) {
        this.netOut = netOut;
        this.myStateQueue = myStateQueue;
    }

    @Override
    public Void call() throws Exception {
        while(true){
            GameState gameState = myStateQueue.take();
            //Without reset values inside map wont get updated on receiving end.
            netOut.reset();
            netOut.writeObject(gameState);
            netOut.flush();
        }
    }
}
