package client;

import client.entities.Player;
import general.GameState;
import general.PlayerInputState;

import general.PlayerState;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import server.ServerMazeMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class InGame extends BasicGameState {
    private GameState gameState = new GameState(-1, 0);
    private boolean multiplayer = true;
    private boolean communicatorCreated = false;
    private BlockingQueue<GameState> receiveData = new LinkedBlockingQueue<>();
    private BlockingQueue<PlayerInputState> sendData = new LinkedBlockingQueue<>();
    private ServerMazeMap smap = new ServerMazeMap(800,600); //for testing. Server has to send it
    private MazeMap map;
    private StartScreen startScreen;
    private boolean pause = false;
    private Image floorTexture;

    public InGame(StartScreen startScreen) {
        this.startScreen = startScreen;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException{
        container.setAlwaysRender(true);
        map = new MazeMap(smap);
        System.out.println("init done");
        this.floorTexture = new Image("resources/floor800x600.png");

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (multiplayer && !communicatorCreated) {
            new Thread(new Communicator(sendData, receiveData, startScreen.getIP())).start();
            communicatorCreated = true;
        }
        if (pause){
            pause = false;
            game.enterState(3);
        }
        ArrayList<GameState> receivedGameStates = new ArrayList<>();
        receiveData.drainTo(receivedGameStates);
        if(!receivedGameStates.isEmpty()){
            GameState mostRecentReceived = Collections.max(receivedGameStates);
            if(mostRecentReceived.compareTo(gameState) == 1){
                gameState = mostRecentReceived;
            }
        }
        PlayerInputState freshInput = PlayerInputReceiver.receive(container);
        sendData.add(freshInput);
    }





    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(Color.white);
        g.texture(new Rectangle(0,0,800,600), floorTexture, 1, 1,true);
        map.render(container, game, g);
        for(Map.Entry<Integer, PlayerState> entry : gameState.getPlayerStates().entrySet()){
            Player player = new Player(entry.getValue());
            player.render(container, g);
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if (c == 'p'){
            pause = true;
        }
    }

    @Override
    public int getID() {
        return 0;
    }
}