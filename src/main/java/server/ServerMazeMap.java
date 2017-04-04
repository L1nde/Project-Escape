package server;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by Meelis Perli on 4/2/2017.
 */
public class ServerMazeMap {
    private final int width;
    private final int height;
    private String[][] map;

    public ServerMazeMap(int width, int height) {
        this.width = width/20;
        this.height = height/20;
        this.map = generateMap();
    }

    private String[][] generateMap() {
        map = new String[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //sides
                if(i == 0 || j == 0 || i == width-1 || j == height-1){
                    map[i][j] = "W";
                }
            }
        }
        return map;
    }
    public String[][] getMap() {
        return this.map;
    }
}
