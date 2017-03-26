package client;/*
 * Created by L1ND3 on 23.03.2017. 
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class Communicator implements Runnable {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private BlockingQueue<String> receiveData;
    private BlockingQueue<String> sendData;

    public Communicator(BlockingQueue<String> sendData, BlockingQueue<String> receiveData) {
        try {
            this.socket = new Socket("localhost", 1337);
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.sendData = sendData;
            this.receiveData = receiveData;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                System.out.println(12);
                String s = sendData.take();
                System.out.println(s);
                dos.writeUTF(s);
                System.out.println(13);
                String u = dis.readUTF();
                System.out.println(1);
                receiveData.offer(u);
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
