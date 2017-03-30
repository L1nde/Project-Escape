package server;

import general.PlayerInputState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;

public class ServerReceiver implements Callable<Void>{
    final private int id;
    final private ConcurrentMap<Integer, PlayerInputState> lastInputs;
    final private ObjectInputStream netIn;

    public ServerReceiver(int id, ObjectInputStream netIn, ConcurrentMap<Integer, PlayerInputState> lastInputs) {
        this.id = id;
        this.lastInputs = lastInputs;
        this.netIn = netIn;
    }

    @Override
    public Void call() throws IOException, ClassNotFoundException {
        while(true){
            PlayerInputState receivedInput = (PlayerInputState) netIn.readObject();
            lastInputs.put(id, receivedInput);
        }
    }
}
