package client;

import general.PlayerInputState;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class Sender implements Callable<Void>{
    final private ObjectOutputStream netOut;
    final private BlockingQueue<PlayerInputState> queueIn;

    public Sender(BlockingQueue<PlayerInputState> queueIn, ObjectOutputStream netOut) {
        this.netOut = netOut;
        this.queueIn = queueIn;
    }

    @Override
    public Void call() throws IOException, ClassNotFoundException {
        while(true){
            try{
                PlayerInputState receivedOutput = queueIn.take();
                netOut.reset();
                netOut.writeObject(receivedOutput);
                netOut.flush();
            } catch (InterruptedException exp){
                throw new RuntimeException(exp);
            }
        }
    }
}

