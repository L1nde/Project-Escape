package client;

import client.entities.Ghost;
import client.entities.Player;
import general.GameState;
import general.GhostState;
import server.Ghosts.GhostObjects;
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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class InGame extends BasicGameState {
    private GameState gameState;
    private boolean multiplayer = true;
    private boolean communicatorCreated = false;
    private BlockingQueue<GameState> receiveData = new LinkedBlockingQueue<>();
    private BlockingQueue<PlayerInputState> sendData = new LinkedBlockingQueue<>();
    private BlockingQueue<ServerMazeMap> smap = new ArrayBlockingQueue<>(1);
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
        System.out.println("init done");
        this.floorTexture = new Image("resources/floor800x600.png");

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (multiplayer && !communicatorCreated) {
            new Thread(new Communicator(sendData, receiveData, startScreen.getIP(), smap)).start();
            communicatorCreated = true;
            try {
                map = new MazeMap(smap.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (pause){
            pause = false;
            game.enterState(3);
        }
        ArrayList<GameState> receivedGameStates = new ArrayList<>();
        receiveData.drainTo(receivedGameStates);
        if(!receivedGameStates.isEmpty()){
            GameState mostRecentReceived = Collections.max(receivedGameStates);
            if(gameState == null || mostRecentReceived.compareTo(gameState) == 1){
                gameState = mostRecentReceived;
            }
        }if(gameState != null){
            map.update(gameState.getMapUpdate());
        }
        PlayerInputState freshInput = PlayerInputReceiver.receive(container);
        sendData.add(freshInput);
    }


    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(Color.white);
        g.texture(new Rectangle(0,0,800,600), floorTexture, 1, 1,true);
        map.render(container, game, g);
        if(gameState != null){
            for(Map.Entry<Integer, PlayerState> entry : gameState.getPlayerStates().entrySet()){
                Player player = new Player(entry.getValue());
                player.render(container, g);
            }
            for (Map.Entry<Integer, GhostState> entry : gameState.getGhostsStates().entrySet()) {
                Ghost ghost = new Ghost(entry.getValue());
                ghost.render(container, g);
            }
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