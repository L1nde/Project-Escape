package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class Server {
    public static void main(String[] args) throws IOException {

        try (ServerSocket ss = new ServerSocket(1337)) {
            while (true) {
                try {
                    Socket sock = ss.accept();
                    //   new Thread(new Echo(sock)).start();

                } catch (IOException e) {
                    System.out.println(e.getMessage()); // nii ikka võis teha?
                }
            }
        }
    }
}
