package server;

import general.MapPoint;
import general.MapUpdate;
import general.Point;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Meelis Perli on 4/2/2017.
 */
public class ServerMazeMap implements Serializable {
    private final int width;
    private final int height;
    private final String[][] map;
    private MapUpdate mapUpdate;

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

    public boolean inWall(MapPoint toCheck){
        return map[toCheck.getX()][toCheck.getY()].equals("W");
    }

    public Point findRandomValidPoint(Point center, float rectRange){
        Random rng = ThreadLocalRandom.current();
        MapPoint res;
        do{
            res = (new Point(center.getX() + (rng.nextFloat()*(rectRange*2)- rectRange),
                    center.getY() + (rng.nextFloat()*(rectRange*2)- rectRange))).getMapPoint();

        }while(inWall(res));
        return res.getPoint();
    }
    public List<Point> findShortestPath(Point begin, Point end){
        MapPoint mapBegin = begin.getMapPoint();
        MapPoint mapEnd = begin.getMapPoint();
        if(inWall(mapBegin)){
            throw new IllegalArgumentException("Beginning is inside a wall");
        }
        if(inWall(mapEnd)){
            throw new IllegalArgumentException("Ending is inside a wall");
        }
        MapPoint.clearVisits();
        Queue<MapMovement> horizon = new ArrayDeque<>();
        mapBegin.visit();
        horizon.add(new MapMovement(mapBegin, null));
        while(!horizon.isEmpty()){
            MapMovement curPath = horizon.remove();
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
}
