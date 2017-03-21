package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class Server {
    public static void main(String[] args) throws IOException {
        List<Thread> threadList = new ArrayList<>();
        List<BlockingQueue<String>> data = new ArrayList<>();
        int id = -1;
        try (ServerSocket ss = new ServerSocket(1337)) {
            while (true) {
                try {
                    Socket sock = ss.accept();
                    data.add(new ArrayBlockingQueue<>(10));
                    threadList.add(new Thread(new ServerMain(sock, data, id++)));
                    threadList.get(threadList.size()-1).start();


                } catch (IOException e) {
                    System.out.println(e.getMessage()); // nii ikka võis teha?
                }
            }
        }
    }
}