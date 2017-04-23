package server;

import general.Ghosts.Tile;
import general.MapUpdate;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import java.io.Serializable;

/**
 * Created by Meelis Perli on 4/2/2017.
 */
public class ServerMazeMap implements Serializable {
    private final int width;
    private final int height;
    private final String[][] map;
    private MapUpdate mapUpdate;
    private Tile[][] tiles;

    public ServerMazeMap(int width, int height) {
        this.width = width/20;
        this.height = height/20;
        this.map = generateMap();
    }

    public void setMapUpdate(MapUpdate mapUpdate) {
        this.mapUpdate = mapUpdate;
    }

    public MapUpdate getMapUpdate() {
        return mapUpdate;
    }

    private String[][] generateMap() {
        String[][] tempMap = new String[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //sides
                if(i == 0 || j == 0 || i == width-1 || j == height-1){
                    tempMap[i][j] = "W";
                }
                if (Math.random()>0.85){
                    tempMap[i][j] = "W";
                } else if (tempMap[i][j] == null){
                    tempMap[i][j] = "F";
                }
//
            }
//
        }
        return tempMap;
    }
    public String[][] getMap() {
        return this.map;
    }
}
