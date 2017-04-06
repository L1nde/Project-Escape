package client;

import client.entities.Food;
import client.entities.Wall;
import general.MapUpdate;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import server.ServerMazeMap;

/**
 * Created by Meelis on 03/04/2017.
 */
public class MazeMap {
    private MapObjects[][] map;

    public MazeMap(ServerMazeMap map)  {
        String[][] map1 = map.getMap();
        this.map = new MapObjects[map1.length][map1[0].length];
        System.out.println("kek");
        for (int x = 0; x < map1.length; x++) {
            for (int y = 0; y < map1[x].length; y++) {

                if (map1[x][y] != null) {
                    if (map1[x][y].equals("W")) {
                        this.map[x][y] = new Wall   (new int[]{20 * x, 20 * y});
                    } else if (map1[x][y].equals("F")){
                        this.map[x][y] = new Food(new int[]{20 * x, 20 * y});
                    }
                }
            }
        }
    }

    public void update(MapUpdate mapUpdate) {

        if (mapUpdate != null) {
            this.map[mapUpdate.getX()][mapUpdate.getY()] = null;
        }

    }

    public void render(GameContainer gc, StateBasedGame game, Graphics g) {
        for (MapObjects[] mapObjects : map) {
            for (MapObjects mapObject : mapObjects) {
                if (mapObject != null) {
                    mapObject.render(gc, game, g);
                }
            }
        }
    }

}
