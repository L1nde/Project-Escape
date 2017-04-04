package client;/*
 * Created by L1ND3 on 03.04.2017. 
 */


import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import server.Server;

import java.io.IOException;

public class StartScreen extends BasicGameState{
    private TextField ipBox;
    private Rectangle connectButton;
    private Rectangle hostButton;
    private boolean join = false;
    private boolean host = false;
    private String ip = "localhost";

    @Override
    public int getID() {
        return 2;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        ipBox = new TextField(container, container.getDefaultFont(),100,50, 200,20);
        connectButton = new Rectangle(305, 50, 100, 20);
        hostButton = new Rectangle(200, 90, 100, 20);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setBackground(Color.blue);
        g.setColor(Color.green);
        ipBox.render(container, g);
        g.draw(connectButton);
        g.drawString("Connect", connectButton.getX()+5, connectButton.getY()+1);
        g.draw(hostButton);
        g.drawString("Host", hostButton.getX()+5, hostButton.getY()+1);
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        if (button == 0 && x >= connectButton.getX() && x <= connectButton.getMaxX() && y >= connectButton.getY() && y <= connectButton.getMaxY()){
            join = true;
        }
        else if (button == 0 && x >= hostButton.getX() && x <= hostButton.getMaxX() && y >= hostButton.getY() && y <= hostButton.getMaxY()){
            host = true;
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (join){
            ip = ipBox.getText();
            game.enterState(0);
        }
        if (host){
            host = false;
            new Thread(new hostThread()).start();
            game.enterState(0);
        }
    }

    public String getIP(){
        return ip;
    }
}
