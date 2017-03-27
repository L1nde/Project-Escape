package client;

import client.entities.Player;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class InGame extends BasicGameState {

    private List<Player> players = new ArrayList<>();
    private int playerCount = 1;
    private boolean multiplayer = true;
    private BlockingQueue<String> receiveData = new ArrayBlockingQueue<>(1);
    private BlockingQueue<String> sendData = new ArrayBlockingQueue<>(1);

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException{
        container.setAlwaysRender(true);
        if (multiplayer) {
            new Thread(new Communicator(sendData, receiveData)).start();
            try {
                players.add(new Player(100, 100));
                sendData.put("0/100/100");
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
            String input = receiveData.poll();
        if (input != null){
            String[] dataInput = input.split(" ");
            for (String s : dataInput) {
                String[] dataInput2 = s.split("/");
                int id = Integer.parseInt(dataInput2[0]);
                if (id == playerCount){
                    players.add(new Player(100, 100));
                    playerCount++;
                    System.out.println("added");
                }
                if (id <= playerCount) {
                    players.get(id).setX(Float.parseFloat(dataInput2[1]));
                    players.get(id).setY(Float.parseFloat(dataInput2[2]));
                }
            }

            }
            String data = players.get(0).update(container);
            sendData.offer(data);

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