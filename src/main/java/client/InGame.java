package client;

import client.entities.Player;
import general.GameState;
import general.PlayerInputState;

import general.PlayerState;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
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
    private int playerCount = 1;
    private boolean multiplayer = true;
    private boolean communicatorCreated = false;
    private BlockingQueue<GameState> receiveData = new LinkedBlockingQueue<>();
    private BlockingQueue<PlayerInputState> sendData = new LinkedBlockingQueue<>();
    private ServerMazeMap smap = new ServerMazeMap(800,600); //for testing. Server has to send it
    private MazeMap map;
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException{
        container.setAlwaysRender(true);
        map = new MazeMap(smap);
        System.out.println("init done");

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (multiplayer && !communicatorCreated) {
            new Thread(new Communicator(sendData, receiveData)).start();
            communicatorCreated = true;
        }
        ArrayList<GameState> receivedGameStates = new ArrayList<>();
        receiveData.drainTo(receivedGameStates);
        if(!receivedGameStates.isEmpty()){
            GameState mostRecentReceived = Collections.max(receivedGameStates);
            if(mostRecentReceived.compareTo(gameState) == 1){
                gameState = mostRecentReceived;
            }
        }
        /*
        String input = receiveData.poll();
        if (input != null){
            String[] dataInput = input.split(" ");
            for (String s : dataInput) {
                String[] dataInput2 = s.split("/");
                int id = Integer.parseInt(dataInput2[0]);
                if (id == playerCount){
                    players.add(new Player(100, 100));
                    playerCount++;
                    System.out.println("added");
                }
                if (id <= playerCount) {
                    players.get(id).setX(Float.parseFloat(dataInput2[1]));
                    players.get(id).setY(Float.parseFloat(dataInput2[2]));
                }
            }
        }
        */
        PlayerInputState freshInput = PlayerInputReceiver.receive(container);
        sendData.add(freshInput);
    }





    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(Color.green);
        g.fillRect(0,0, container.getWidth(), container.getHeight());
        map.render(container, game, g);
        for(Map.Entry<Integer, PlayerState> entry : gameState.getPlayerStates().entrySet()){
            Player player = new Player(entry.getValue());
            player.render(container, g);
        }
    }

    @Override
    public int getID() {
        return 0;
    }
}