package client;/*
 * Created by L1ND3 on 04.04.2017. 
 */

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class pauseScreen extends BasicGameState {
    private boolean pause = true;

    @Override
    public int getID() {
        return 3;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (!pause){
            pause = true;
            game.enterState(0);
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if (c == 'p'){
            pause = false;
        }
    }
}
