package server;

import jdk.nashorn.internal.ir.Block;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class ServerMain implements Runnable {
    private Socket sock;
    private List<BlockingQueue<String>> data;
    private float speed = 1;
    private int id;
    private float[] playersCoords = {100, 100};


    public ServerMain(Socket sock, List<BlockingQueue<String>> data, int id) {
        this.sock = sock;
        this.data = data;
        this.id = id;
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(sock.getInputStream())) {
            try (DataOutputStream dos = new DataOutputStream(sock.getOutputStream())) {
                while(true) {
                    String receiveData = dis.readUTF();
                    for (BlockingQueue<String> datum : data) {
                        if (datum.remainingCapacity() == 0){
                            datum.poll();
                        }
                        datum.put(calculatePositions(receiveData));
                    }


                    System.out.println(data);
                    String temp = data.get(id).take();
                    dos.writeUTF(temp);
                    dos.flush();
                }
            } catch (IOException | InterruptedException e) {
//                throw new RuntimeException(e); //might be wrong, please fix if you know how to do it better
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
//            throw new RuntimeException(e); //might be wrong, please fix if you know how to do it better
            System.out.println(e.getMessage());
        }
    }

    public String calculatePositions(String input){
        if (input.contains("up")){
            playersCoords[1] -= speed;
        }
        if (input.contains("down")){
            playersCoords[1] += speed;
        }
        if (input.contains("left")){
            playersCoords[0] -= speed;
        }
        if (input.contains("right")){
            playersCoords[0] += speed;
        }
        return String.valueOf(id + "/" + playersCoords[0] + "/" + playersCoords[1]);
    }
}

