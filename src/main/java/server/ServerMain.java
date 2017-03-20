package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class ServerMain implements Runnable {
    private Socket sock;

    public ServerMain(Socket sock) {
        this.sock = sock;
    }

    @Override
    public void run() {
        try (DataInputStream is = new DataInputStream(sock.getInputStream())) {
            try (DataOutputStream os = new DataOutputStream(sock.getOutputStream())) {
                while(true) {
                    String s = is.readUTF();

                }

            } catch (IOException e) {
                throw new RuntimeException(e); //might be wrong, please fix if you know how to do it better
            }
        } catch (IOException e) {
            throw new RuntimeException(e); //might be wrong, please fix if you know how to do it better
        }
    }

}

