package client;/*
 * Created by L1ND3 on 03.04.2017. 
 */

import server.Server;

import java.io.IOException;

public class HostThread implements Runnable{
    @Override
    public void run() {
        try {
            Server.main(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
