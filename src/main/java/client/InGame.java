package client;

import client.entities.Player;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class InGame extends BasicGameState {

    private List<Player> players = new ArrayList<>();
    private int playerCount = 1;
    private boolean multiplayer = true;
    private BlockingQueue<String> receiveData = new ArrayBlockingQueue<String>(1);
    private BlockingQueue<String> sendData = new ArrayBlockingQueue<String>(1);

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException{
        container.setAlwaysRender(true);
        if (multiplayer) {
           new Thread(new Communicator(sendData, receiveData)).start();
            try {
                players.add(new Player(100, 100));
                receiveData.put("0/100/100");
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        try {



            String input = receiveData.take();
            String[] dataInput = input.split("/");
            int id = Integer.parseInt(dataInput[0]);
            if (id == playerCount){
                players.add(new Player(100, 100));
                playerCount++;
            }
            String data = players.get(0).update(container);
            sendData.put(data);
            for (int i = 0; i < players.size(); i++) {
                if(id == i){
                    players.get(i).setX(Float.parseFloat(dataInput[1]));
                    players.get(i).setY(Float.parseFloat(dataInput[2]));
                }
            }


        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(Color.green);
        g.fillRect(0,0, container.getWidth(), container.getHeight());
        for (Player player : players) {
            player.render(container, g);
        }
    }

    @Override
    public int getID() {
        return 0;
    }
}
