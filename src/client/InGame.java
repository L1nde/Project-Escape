package client;

import client.entities.Player;
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

/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class InGame extends BasicGameState {

    private Player player;
    private boolean multiplayer = true;
    private Socket sock;

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException{
        player = new Player(100,100,1);
        if (multiplayer) {
            try {
                this.sock = new Socket("localhost", 1337);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        player.update(container, sock);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(Color.green);
        g.fillRect(0,0, container.getWidth(), container.getHeight());
        player.render(container, g);
    }
}
