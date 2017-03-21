package client;

import client.entities.Player;
import client.entities.otherPlayer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class InGame extends BasicGameState {

    private Player player;
    private int playersNumber = 1;
    private List<otherPlayer> otherPlayers = new ArrayList<>();
    private boolean multiplayer = true;
    private Socket sock;
    private DataInputStream dis;
    private DataOutputStream dos;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException{
        player = new Player(100,100,1);
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
    public int getID() {
        return 0;
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        player.update(container, sock);
        try {
            String s = String.valueOf(player.getX() + "/" + player.getY());
            dos.writeUTF(s);
            dos.flush();
            String input = dis.readUTF();
            String[] data = input.split("/");
            int temp = Integer.parseInt(data[0]);
            int id = Integer.parseInt(data[1]);
            if (playersNumber < temp){
                playersNumber = temp;
                otherPlayers.add(new otherPlayer(playersNumber*100, 100, 1));
            }
            System.out.println(otherPlayers.size() + "|" + playersNumber + "|" + id);
            if (playersNumber > 1){
                for (int i = 0; i < otherPlayers.size(); i++) {
                    otherPlayer otherPlayer = otherPlayers.get(i);
                    if (!data[0].equals("null") && !data[1].equals("null") && !data[2].equals("null") && !data[3].equals("null") && !data[0].equals("-1") && !data[1].equals("-1") && !data[2].equals("-1") && !data[3].equals("-1")) {

                            otherPlayer.setX(Float.parseFloat(data[2]));
                            otherPlayer.setY(Float.parseFloat(data[3]));



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
        player.render(container, g);

            for (otherPlayer otherPlayer : otherPlayers) {
                otherPlayer.render(container, g);

        }
    }
}
