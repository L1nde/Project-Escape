package server;

import general.MapUpdate;
import general.Point;
import general.TileType;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Meelis Perli on 4/2/2017.
 */
public class ServerMazeMap implements Serializable {
    private final int width;
    private final int height;
    private final TileType[][] map;

    public ServerMazeMap(int width, int height) {
        this.width = width/20;
        this.height = height/20;
        this.map = generateMap();
    }

    private TileType[][] generateMap() {
        TileType[][] tempMap = new TileType[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //sides
                if(i == 0 || j == 0 || i == width-1 || j == height-1){
                    tempMap[i][j] = TileType.WALL;
                }
                if (Math.random()>0.85){
                    tempMap[i][j] = TileType.WALL;
                } else if (tempMap[i][j] == null){
                    tempMap[i][j] = TileType.FOOD;
                }
            }
        }
        return tempMap;
    }

    public TileType getTile(MapPoint toCheck){
        if(toCheck.getX() < 0 || toCheck.getX() >= map.length ||
                toCheck.getY() < 0 || toCheck.getY() >= map[toCheck.getX()].length){
            return TileType.WALL;
        }
        return map[toCheck.getX()][toCheck.getY()];
    }

    public void apply(MapUpdate upd){
        if(upd.getX() >= 0 && upd.getX() < map.length ||
                upd.getY() >= 0 || upd.getY() < map[upd.getX()].length){
            map[upd.getX()][upd.getY()] = upd.getTileType();
        }
    }


    public Point findRandomValidPoint(Point center, double rectRange){
        Random rng = ThreadLocalRandom.current();
        MapPoint res;
        do{
            res = new MapPoint(new Point(center.getX() + (rng.nextFloat()*(rectRange*2)- rectRange),
                    center.getY() + (rng.nextFloat()*(rectRange*2)- rectRange)));

        }while(getTile(res) == TileType.WALL);
        return res.getPoint();
    }

    /*
    Use Dijkstras algorithm(Breadth-first search currently)
    To find shortest path via centre Points.
     */
    public List<Point> findShortestPath(Point begin, Point end){
        MapPoint mapBegin = new MapPoint(begin);
        MapPoint mapEnd = new MapPoint(end);
        if(getTile(mapBegin) == TileType.WALL){
            throw new IllegalArgumentException("Beginning is inside a wall");
        }
        if(getTile(mapEnd) == TileType.WALL){
            throw new IllegalArgumentException("Ending is inside a wall");
        }
        MapPoint.clearVisits();
        Queue<MapMovement> horizon = new ArrayDeque<>();
        mapBegin.visit();
        horizon.add(new MapMovement(mapBegin, null));
        while(!horizon.isEmpty()){
            MapMovement curPath = horizon.poll();
            MapPoint curLoc = curPath.getEnd();
            if(curLoc.equals(mapEnd)){
                List<MapPoint> finalPath = curPath.getPath();
                List<Point> res = new LinkedList<>();
                res.add(begin);
                for(MapPoint cur : finalPath){
                    res.add(cur.getPoint());
                }
                res.add(end);
                return res;
            }
            List<MapPoint> neighbours = curLoc.neighbours(this);
            for(MapPoint nxt : neighbours){
                if(!nxt.isVisited()){
                    nxt.visit();
                    horizon.add(new MapMovement(nxt, curPath));
                }
            }
        }
        return new LinkedList<>();
    }

    /**
     * Return absolute value of guaranteed unblocked movement in Direction
     */
    public Double getFreeXRange(Point loc, Double dir){
        MapPoint idx = new MapPoint(loc);
        Point centre = idx.getPoint();
        if(Math.abs(loc.getX() - centre.getX()) > ServerTicker.EPS){
            if((loc.getX() > centre.getX()) == (dir < 0)){
                return Math.abs(centre.getX() - loc.getX());
            }
            MapPoint nxtIdx;
            if(loc.getX() > centre.getX()){
                nxtIdx = idx.right();
            } else {
                nxtIdx = idx.left();
            }
            Point nxtCentre = nxtIdx.getPoint();
            return Math.abs(nxtCentre.getX() - loc.getX());
        }
        //Collisions are checked only on Border.
        MapPoint nxtIdx;
        if(dir > 0){
            nxtIdx = idx.right();
        } else {
            nxtIdx = idx.left();
        }
        if(getTile(nxtIdx) == TileType.WALL){
            return 0.0;
        }
        if(loc.getY() - centre.getY() > ServerTicker.EPS && getTile(nxtIdx.up()) == TileType.WALL){
            return 0.0;
        }
        if(centre.getY() - loc.getY() > ServerTicker.EPS && getTile(nxtIdx.down()) == TileType.WALL){
            return 0.0;
        }
        return Math.abs(nxtIdx.getPoint().getX() - loc.getX());
    }

    /**
     * Return absolute value of guaranteed unblocked movement in Direction
     */
    public Double getFreeYRange(Point loc, Double dir){
        MapPoint idx = new MapPoint(loc);
        Point centre = idx.getPoint();
        if(Math.abs(loc.getY() - centre.getY()) > ServerTicker.EPS){
            if((loc.getY() > centre.getY()) == (dir < 0)){
                return Math.abs(centre.getY() - loc.getY());
            }
            MapPoint nxtIdx;
            if(loc.getY() > centre.getY()){
                nxtIdx = idx.up();
            } else {
                nxtIdx = idx.down();
            }
            Point nxtCentre = nxtIdx.getPoint();
            return Math.abs(nxtCentre.getY() - loc.getY());
        }
        //Collisions are checked only on Border.
        MapPoint nxtIdx;
        if(dir > 0){
            nxtIdx = idx.up();
        } else {
            nxtIdx = idx.down();
        }
        if(getTile(nxtIdx) == TileType.WALL){
            return 0.0;
        }
        if(loc.getX() - centre.getX() > ServerTicker.EPS && getTile(nxtIdx.right())  == TileType.WALL){
            return 0.0;
        }
        if(centre.getX() - loc.getX() > ServerTicker.EPS && getTile(nxtIdx.left()) == TileType.WALL){
            return 0.0;
        }
        return Math.abs(nxtIdx.getPoint().getY() - loc.getY());
    }

    public List<MapUpdate> getAsUpdates(){
        List<MapUpdate> res = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                res.add(new MapUpdate(i, j, map[i][j]));
            }
        }
        return res;
    }
}
