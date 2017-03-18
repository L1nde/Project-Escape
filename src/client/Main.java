package client;

import client.InGame;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends StateBasedGame {

    private static int screenW = 1280;
    private static int screenH = 800;
    private Main(String AppName){
        super(AppName);
    }

    public void initStatesList(GameContainer gc) throws SlickException {

        this.addState(new InGame());

    }

    public static void main(String[] args) {

        try
        {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new Main("Project Escape"));
            appgc.setDisplayMode(screenW, screenH, false);
            appgc.start();
        }
        catch (SlickException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}