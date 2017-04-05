package server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class ServerCommunicator implements Runnable{
    final private Map<UUID, Integer> privateToPublicID;
    final private Map<Integer, Thread> communicatorThreads;
    final private int freePublicId;
    final private Socket sock;
    final private ServerTicker ticker;
    private final ServerMazeMap map;

    public ServerCommunicator(Map<UUID, Integer> privateToPublicID, //Needs to be synchronized
                              Map<Integer, Thread> communicatorThreads, //Needs to be synchronized
                              int freePublicId,
                              Socket sock,
                              ServerTicker ticker,
                              ServerMazeMap map) throws SocketException {
        this.privateToPublicID = privateToPublicID;
        this.communicatorThreads = communicatorThreads;
        this.freePublicId = freePublicId;
        this.sock = sock;
        this.ticker = ticker;
        sock.setSoTimeout(1000);
        this.map = map;

    }

    @Override
    public void run(){
        try(ObjectInputStream netIn = new ObjectInputStream(sock.getInputStream());
                ObjectOutputStream netOut = new ObjectOutputStream(sock.getOutputStream())){
            UUID uuid = (UUID)netIn.readObject();
            synchronized (privateToPublicID){
                if(!privateToPublicID.containsKey(uuid)){
                    privateToPublicID.put(uuid, freePublicId);
                    ticker.addPlayer(freePublicId);
                }
            }
            int id = privateToPublicID.get(uuid);
            netOut.writeInt(id);
            netOut.flush();
            //Old communicators must be stopped so the don't cause any further poll
            synchronized (communicatorThreads){
                if(communicatorThreads.containsKey(id)){
                    Thread oldCommunicator = communicatorThreads.get(id);
                    //Also interrupts children
                    oldCommunicator.interrupt();
                }
            }

            communicatorThreads.put(id, Thread.currentThread());
            ServerReceiver receiver = new ServerReceiver(id, netIn, ticker.getLastInputs());
            ServerSender sender = new ServerSender(ticker.getMyStateQueue(id), netOut, map);
            FutureTask senderTask = new FutureTask(sender);
            Thread senderThread = new Thread(senderTask);
            senderThread.start();
            try{
                receiver.call();
            } finally {
                if(!senderTask.cancel(true)){
                    senderTask.get();
                }
            }
        } catch (Exception exp){
            exp.printStackTrace();
        }
    }
}
