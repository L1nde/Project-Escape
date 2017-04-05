package client;

import general.GameState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class Receiver implements Callable<Void>{
    final private ObjectInputStream netIn;
    final private BlockingQueue<GameState> queueOut;

    public Receiver(ObjectInputStream netIn, BlockingQueue<GameState> queueOut) {
        this.netIn = netIn;
        this.queueOut = queueOut;
    }

    @Override
    public Void call() throws IOException, ClassNotFoundException {
        netIn.readObject();
        while(true){
            GameState receivedInput = (GameState) netIn.readObject();
            queueOut.add(receivedInput);
        }
    }
}

