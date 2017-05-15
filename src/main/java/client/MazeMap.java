package client;

import general.MapUpdate;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by Meelis on 03/04/2017.
 */
public class MazeMap {
    private MapObjects[][] map;

    public MazeMap(int xLength, int yLength){
        map = new MapObjects[xLength][yLength];
    }

    public void update(MapUpdate upd) {
        this.map[upd.getX()][upd.getY()] = TileEntityFactory.getEntity(upd);
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
