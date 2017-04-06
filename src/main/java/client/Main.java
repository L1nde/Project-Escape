package client;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends StateBasedGame {

    private static int screenW = 800;
    private static int screenH = 600;
    private Main(String AppName){
        super(AppName);
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        this.addState(new Menu());
        StartScreen startScreen = new StartScreen();
        this.addState(startScreen);
        this.addState(new InGame(startScreen));
        this.addState(new pauseScreen());
    }

    public static void main(String[] args) {
        try
        {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new Main("Project Escape"));
            appgc.setDisplayMode(screenW, screenH, false);
            appgc.setTargetFrameRate(6000);
            appgc.start();
        }
        catch (SlickException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
