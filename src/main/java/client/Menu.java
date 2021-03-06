package client;/*
 * Created by L1ND3 on 18.03.2017. 
 */

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class Menu extends BasicGameState {
    private Rectangle playButton;
    private Rectangle optionsButton;
    private Rectangle exitButton;
    private boolean playMP = false;
    private boolean exit = false;
    private Image background;

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        playButton = new Rectangle(75, 100, 100, 40);
        optionsButton = new Rectangle(playButton.getX(), playButton.getY() + 50, 100, 40);
        exitButton = new Rectangle(optionsButton.getX(), optionsButton.getY() + 50, 100, 40);
        background = new Image("src/main/resources/textures/Menu920x600.png");
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.texture(new Rectangle(0, 0, 920, 600), background, 1, 1, true);

        g.draw(playButton);
        g.drawString("Play", playButton.getX()+10, playButton.getY()+10);

        g.draw(optionsButton);
        g.drawString("Options", optionsButton.getX()+10, optionsButton.getY()+10);

        g.draw(exitButton);
        g.drawString("Exit", exitButton.getX()+10, exitButton.getY()+10);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (playMP){
            playMP = false;
            game.enterState(2);
        }
        else if (exit){
            container.exit();
        }
    }

    @Override
    public void mousePressed(int button, int x, int y){
        if (button == 0 && x >= playButton.getX() && x <= playButton.getMaxX() && y >= playButton.getY() && y <= playButton.getMaxY()){
            playMP = true;
        }
        else if (button == 0 && x >= exitButton.getX() && x <= exitButton.getMaxX() && y >= exitButton.getY() && y <= exitButton.getMaxY()){
            exit = true;
        }
    }
}
