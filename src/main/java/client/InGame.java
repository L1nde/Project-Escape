package client;

import client.entities.Ghost;
import client.entities.GhostRusher;
import client.entities.Player;
import general.*;

import general.GameState;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Created by Meelis Perli on 3/18/2017.
 */
public class InGame extends BasicGameState {
    private GameState gameState;
    private boolean multiplayer = true;
    private Communicator communicator = null;
    private BlockingQueue<GameState> receiveData = new LinkedBlockingQueue<>();
    private BlockingQueue<PlayerInputState> sendData = new LinkedBlockingQueue<>();
    private MazeMap map = new MazeMap(40, 30);
    private StartScreen startScreen;
    private boolean pause = false;
    private Image floorTexture;
    private Music music;

    public InGame(StartScreen startScreen) {
        this.startScreen = startScreen;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException{
        container.setAlwaysRender(true);
        System.out.println("init done");
        this.floorTexture = new Image("src/main/resources/textures/background920x600.png");
        music = new Music("src/main/resources/music/ElevatorMusic.ogg");


    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (multiplayer && communicator == null) {
            communicator = new Communicator(sendData, receiveData, startScreen.getIP());
            new Thread(communicator).start();
            music.loop();
        }
        if (pause){
            pause = false;
            game.enterState(3);
        }
        ArrayList<GameState> receivedGameStates = new ArrayList<>();
        receiveData.drainTo(receivedGameStates);
        Collections.sort(receivedGameStates);
        for(GameState state : receivedGameStates){
            if(gameState == null || state.compareTo(gameState) != -1){
                for(MapUpdate cur : state.getMapUpdates()){
                    map.update(cur);
                }
            }
        }
        if(!receivedGameStates.isEmpty()){
            GameState mostRecentReceived = receivedGameStates.get(receivedGameStates.size()-1);
            if(gameState == null || mostRecentReceived.compareTo(gameState) == 1){
                gameState = mostRecentReceived;
            }
        }
        PlayerInputState freshInput = PlayerInputReceiver.receive(container);
        sendData.add(freshInput);
    }


    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(Color.white);
        g.texture(new Rectangle(0,0,920,600), floorTexture, 1, 1,true);
        map.render(container, game, g);
        if(gameState != null){
            for(Map.Entry<Integer, PlayerState> entry : gameState.getPlayerStates().entrySet()){
							if (entry.getValue().getLives() != 0){
                Player player = new Player(entry.getValue());
                player.render(container, g);
							}
            }
            for (Map.Entry<Integer, GhostState> entry : gameState.getGhostsStates().entrySet()) {
                if (entry.getValue().getType() == GhostType.RUSHER) {
                    GhostRusher ghost = new GhostRusher(entry.getValue());
                    ghost.render(container, g);
                } else {
                    Ghost ghost = new Ghost(entry.getValue());
                    ghost.render(container, g);
                }

            }
            long milliseconds = gameState.getTime();
            long seconds = milliseconds/1000;
            int id = communicator.getId();
            g.drawString("Time: " + (((seconds/60)%60) <= 9 ? "0" : "") + (seconds/60)%60 + ":" + ((seconds%60) <= 9 ? "0" : "") + seconds%60, 810, 10);
            if(id != -1){
                if(gameState.getPlayerStates().containsKey(id)){
                    PlayerState curPlayer = gameState.getPlayerStates().get(id);
                    g.drawString("Lives:" + curPlayer.getLives(), 810, 30);
                    g.drawString("Score:" + curPlayer.getScore(), 810, 50);
                }
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
