package client;

import client.entities.*;
import general.MapUpdate;
import general.Point;

public class TileEntityFactory {
    public static MapObjects getEntity(MapUpdate upd){
        Point loc = new Point(20*upd.getX()+10, 20*upd.getY()+10);
        switch (upd.getTileType()){
            case WALL: return new Wall(loc);
            case FOOD: return new Food(loc);
            case EMPTY: return null;
        }
        return null;
    }
}
