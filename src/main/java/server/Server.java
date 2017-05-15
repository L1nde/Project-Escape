package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class Server {
    public static void main(String[] args) throws IOException {
        List<Thread> threadList = new ArrayList<>();
        int freePublicID = 0;
        Map<UUID, Integer> privateToPublicID = Collections.synchronizedMap(new HashMap<UUID, Integer>());
        Map<Integer, Thread> communicatorThreads = Collections.synchronizedMap(new HashMap<Integer, Thread>());
        ServerMazeMap map = new ServerMazeMap();
        ServerTicker ticker = new ServerTicker(map);
        Thread tickerThread = new Thread(ticker);
        tickerThread.start();
        threadList.add(tickerThread);
        try (ServerSocket ss = new ServerSocket(1337)) {
            while (true) {
                try {
                    Socket sock = ss.accept();
                    ServerCommunicator communicator = new ServerCommunicator(
                            privateToPublicID, communicatorThreads, freePublicID, sock, ticker, map);
                    ++freePublicID;
                    Thread communicatorThread = new Thread(communicator);
                    communicatorThread.start();
                    threadList.add(communicatorThread);
                } catch (IOException exp) {
                    exp.printStackTrace();
                }
            }
        }
    }
}
