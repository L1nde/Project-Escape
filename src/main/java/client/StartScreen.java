package client;/*
 * Created by L1ND3 on 03.04.2017. 
 */


import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StartScreen extends BasicGameState{
    private TextField ipBox;
    private Rectangle connectButton;
    private Rectangle hostButton;
    private Rectangle backButton;
    private boolean join = false;
    private boolean host = false;
    private boolean back = false;
    private String ip = "localhost";
    private Image background;

    @Override
    public int getID() {
        return 2;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        connectButton = new Rectangle(75, 100, 100, 40);
        ipBox = new TextField(container, container.getDefaultFont(),(int)connectButton.getX() + 110,(int)connectButton.getY() + 10, 200,20);
        hostButton = new Rectangle(connectButton.getX(), connectButton.getY() + 50, 100, 40);
        backButton = new Rectangle(hostButton.getX(), hostButton.getY() + 50, 100, 40);
        background = new Image("src/main/resources/textures/Menu920x600.png");
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.texture(new Rectangle(0, 0, 920, 600), background, 1, 1, true);

        ipBox.render(container, g);

        g.draw(connectButton);
        g.drawString("Connect", connectButton.getX()+10, connectButton.getY() + 10);

        g.draw(hostButton);
        g.drawString("Host", hostButton.getX()+10, hostButton.getY()+10);

        g.draw(backButton);
        g.drawString("Back", backButton.getX()+10, backButton.getY()+10);

    }

    @Override
    public void mousePressed(int button, int x, int y) {
        if (button == 0 && x >= connectButton.getX() && x <= connectButton.getMaxX() && y >= connectButton.getY() && y <= connectButton.getMaxY()){
            join = true;
        } else if (button == 0 && x >= hostButton.getX() && x <= hostButton.getMaxX() && y >= hostButton.getY() && y <= hostButton.getMaxY()){
            host = true;
        } else if (button == 0 && x >= backButton.getX() && x <= backButton.getMaxX() && y >= backButton.getY() && y <= backButton.getMaxY()){
            back = true;
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (join){
            join = false;
            ip = ipBox.getText();
            game.enterState(0);
        }
        if (host){
            host = false;
            new Thread(new HostThread()).start();
            game.enterState(0);
        }
        if (back){
            back = false;
            game.enterState(1);
        }
    }

    public String getIP(){
        return ip;
    }
}
