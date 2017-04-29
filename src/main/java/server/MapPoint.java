package server;


import general.Point;
import general.TileType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapPoint {
    private final static Set<MapPoint> visited = new HashSet<>();
    private int x, y;

    public MapPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public MapPoint(Point point) {
        x = (int)(point.getX()/20);
        y = (int)(point.getY()/20);
    }


    public void visit(){
        visited.add(this);
    }

    public boolean isVisited(){
        return visited.contains(this);
    }

    public static void clearVisits(){
        visited.clear();
    }

    public MapPoint up(){
        return new MapPoint(x, y+1);
    }
    public MapPoint down(){
        return new MapPoint(x, y-1);
    }
    public MapPoint left(){
        return new MapPoint(x-1, y);
    }
    public MapPoint right(){
        return new MapPoint(x+1, y);
    }

    public List<MapPoint> neighbours(ServerMazeMap maze){
        List<MapPoint> res = new ArrayList<>();
        {
            if(maze.getTile(left()) != TileType.WALL){
                res.add(left());
            }
        }
        {
            if(maze.getTile(right()) != TileType.WALL){
                res.add(right());
            }
        }
        {
            if(maze.getTile(up()) != TileType.WALL){
                res.add(up());
            }
        }
        {
            if(maze.getTile(down()) != TileType.WALL){
                res.add(down());
            }
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapPoint mapPoint = (MapPoint) o;

        if (x != mapPoint.x) return false;
        return y == mapPoint.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    /**
     * Returns the center of the tile
     */
    public Point getPoint(){
        return new Point(x*20+10, y*20+10);
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
