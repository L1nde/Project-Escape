package client;
/*
 * Created by L1ND3 on 23.03.2017. 
 */

import general.GameState;
import general.PlayerInputState;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;

import static java.lang.Thread.sleep;

public class Communicator implements Runnable {
    private int maxTries = 1000;
    private BlockingQueue<GameState> receiveData;
    private BlockingQueue<PlayerInputState> sendData;
    private String ip;
    private int id = -1;

    public Communicator(BlockingQueue<PlayerInputState> sendData, BlockingQueue<GameState> receiveData,
                        String ip) {
        this.sendData = sendData;
        this.receiveData = receiveData;
        this.ip = ip;
    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        for(int i = 0; i < maxTries; ++i){
            Socket sock = new Socket();
            try {
                sock.connect(new InetSocketAddress(ip, 1337), 1000);
                sock.setSoTimeout(1000);
                //ObjectInputStream tries to gets an object internally when created.
                try(ObjectOutputStream netOut = new ObjectOutputStream(sock.getOutputStream())){
                    netOut.writeObject(uuid);
                    netOut.flush();
                    try(ObjectInputStream netIn = new ObjectInputStream(sock.getInputStream())){
                        int tmpId = netIn.readInt(); //Outside synchronization to avoid blocking by network.
                        synchronized (this){
                            id = tmpId;
                        }
                        Sender sender = new Sender(sendData, netOut);
                        Receiver receiver = new Receiver(netIn, receiveData);
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
                    }
                }
            } catch (Exception exp){
                exp.printStackTrace();
            } finally{
                if(sock.isConnected() && !sock.isClosed()){
                    try{
                        sock.close();
                    } catch (Exception exp){
                        exp.printStackTrace();
                    }
                }
            }
            try{
                sleep(1000);
            } catch (Exception exp){
                throw new RuntimeException(exp);
            }
        }
    }

    public synchronized int getId() {
        return id;
    }
}
