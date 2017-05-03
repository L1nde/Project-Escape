package server.ghosts;/*
 * Created by L1ND3 on 23.04.2017. 
 */

import org.newdawn.slick.util.pathfinding.Path;
import server.ServerMazeMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

public class PathFinder implements Callable<Path>{
    private List<Tile> open = new ArrayList<>();
    private List<Tile> closed = new ArrayList<>();
    private Tile[][] tiles;
    private int sx;
    private int sy;
    private int tx;
    private int ty;
    private String[][] map;

    public PathFinder(int sx, int sy, int tx, int ty, ServerMazeMap map) {
        this.tiles = new Tile[40][30];
        this.sx = sx;
        this.sy = sy;
        this.tx = tx;
        this.ty = ty;
        this.map = map.getMap();
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 30; j++) {
                tiles[i][j] = new Tile(i,j);
            }
        }
    }

    @Override
    public Path call() throws Exception {
        open.clear();
        closed.clear();
        open.add(tiles[sx][sy]);
        while (open.size() != 0){
            Tile current = open.get(0);
            if (current == tiles[tx][ty]){
                break;
            }
            open.remove(current);
            closed.add(current);
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if ((i != 0) && (j != 0)) {
                        continue;
                    }
                    Tile next = tiles[current.getX()+i][current.getY()+j];
                    if (!map[current.getX()+i][current.getY()+j].equals("W")){
                        if (next.getCost() > current.getCost() + 1){
                            open.remove(next);
                            closed.remove(next);
                        }
                        if (!open.contains(next) && !closed.contains(next)){
                            next.setCost(current.getCost() + 1);
                            open.add(next);
                            next.setParent(current);
                            Collections.sort(open);
                        }
                    }
                }
            }
        }
        if (tiles[tx][ty].getParent() == null){
            return null;
        }
        Path path = new Path();
        Tile target = tiles[tx][ty];
        while (target != tiles[sx][sy]){
            path.prependStep(target.getX(), target.getY());
            target = target.getParent();
        }
        path.prependStep(sx, sy);
        return path;
    }
}
