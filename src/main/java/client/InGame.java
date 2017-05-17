package client;

import client.entities.*;
import general.*;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

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
    private boolean exit = false;
    private boolean restart = false;
    private Image floorTexture;
    private Music music;
    private Rectangle exitButton;
    private Rectangle restartButton;

    public InGame(StartScreen startScreen) {
        this.startScreen = startScreen;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException{
        container.setAlwaysRender(true);
        this.floorTexture = new Image("src/main/resources/textures/background2_920x600.png");
        music = new Music("src/main/resources/music/ElevatorMusic.ogg");
        this.exitButton = new Rectangle(container.getWidth()-110, container.getHeight()-50, 100, 40);
        this.restartButton = new Rectangle(exitButton.getX(), exitButton.getY() - 50, 100, 40);
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
        if (restart){
            restart = false;
            freshInput.setRestart();
        }
        sendData.add(freshInput);
    }


    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        if (exit){
            exit = false;
            container.exit();
        }
        g.setColor(Color.white);
        g.texture(new Rectangle(0,0,920,600), floorTexture, 1, 1,true);

        g.draw(exitButton);
        g.drawString("Exit", exitButton.getX() + 10, exitButton.getY() + 10);

        g.draw(restartButton);
        g.drawString("Restart", restartButton.getX() + 10, restartButton.getY() + 10);

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
                } else if (entry.getValue().getType() == GhostType.LEAPER) {
                    GhostLeaper ghost = new GhostLeaper(entry.getValue());
                    ghost.render(container, g);
                } else if (entry.getValue().getType() == GhostType.HUNGRY) {
                    GhostHungry ghost = new GhostHungry(entry.getValue());
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

    public void mousePressed(int button, int x, int y) {
        if (button == 0 && x >= exitButton.getX() && x <= exitButton.getMaxX() && y >= exitButton.getY() && y <= exitButton.getMaxY()) {
            exit = true;
        } else if (button == 0 && x >= restartButton.getX() && x <= restartButton.getMaxX() && y >= restartButton.getY() && y <= restartButton.getMaxY()) {
            restart = true;
        }
    }

    @Override
    public int getID() {
        return 0;
    }
}
