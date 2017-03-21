package server;

import jdk.nashorn.internal.ir.Block;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class ServerMain implements Runnable {
    private Socket sock;
    private List<BlockingQueue<String>> data;
    private int id;

    public ServerMain(Socket sock, List<BlockingQueue<String>> data, int id) {
        this.sock = sock;
        this.data = data;
        this.id = id;
    }

    @Override
    public void run() {
        try (DataInputStream is = new DataInputStream(sock.getInputStream())) {
            try (DataOutputStream os = new DataOutputStream(sock.getOutputStream())) {
                while(true) {
                    data.get(id).offer(is.readUTF());
                    System.out.println(data.size());
                    for (int i = 0; i < data.size(); i++) {
                        if (id != i) {
                            String s = data.get(i).take();
                            System.out.println(s);
                            os.writeUTF(String.valueOf(data.size() + "/" + s));
                            os.flush();
                        } else {
                            os.writeUTF(String.valueOf("-1/-1/-1"));
                            os.flush();

                        }
                    }
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

}

