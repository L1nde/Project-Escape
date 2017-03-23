package client;

import client.entities.Player;
import client.entities.otherPlayer;
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


/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class InGame extends BasicGameState {

    private List<Player> players = new ArrayList<>();
    private int playerCount = 1;
    private boolean multiplayer = true;
    private Socket sock;
    private DataInputStream dis;
    private DataOutputStream dos;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException{
        players.add(new Player(100,100));
        container.setAlwaysRender(true);
        if (multiplayer) {
            try {
                this.sock = new Socket("localhost", 1337);
                this.dis = new DataInputStream(sock.getInputStream());
                this.dos = new DataOutputStream(sock.getOutputStream());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        try {
            String sendData = players.get(0).update(container);
            if (!sendData.equals("nothing")){

                dos.writeUTF(sendData);

                String input = dis.readUTF();
                String[] dataInput = input.split("/");
                int id = Integer.parseInt(dataInput[0]);
                if (id == playerCount){
                    players.add(new Player(100, 100));
                    playerCount++;
                }
                for (int i = 0; i < players.size(); i++) {
                    if(id == i){
                        players.get(i).setX(Float.parseFloat(dataInput[1]));
                        players.get(i).setY(Float.parseFloat(dataInput[2]));
                    }
                }
            }

        } catch (IOException e) {
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
