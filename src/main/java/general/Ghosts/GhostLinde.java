package general.Ghosts;/*
 * Created by L1ND3 on 06.04.2017. 
 */


import general.PlayerState;
import org.newdawn.slick.util.pathfinding.Path;
import server.ServerMazeMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GhostLinde implements GhostObjects, Serializable {
    private float x;
    private float y;
    private float speed;
    private List<Tile> open = new ArrayList<>();
    private List<Tile> closed = new ArrayList<>();
    private Tile[][] tiles;
    private int counter;
    private Path path = null;


    public GhostLinde(float x, float y, float speed, ServerMazeMap smap) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        String[][] map = smap.getMap();
        this.tiles = new Tile[40][30];
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 30; j++) {
                tiles[i][j] = new Tile(i, j);
            }
        }
    }



    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void calculateNewPos(double timeDelta, ServerMazeMap map, Map<Integer, PlayerState> playerStates) {
        if (tiles == null){

        }
        if (path == null){
            int playerCoordsX = (int) Math.floor(playerStates.get(0).getX()/20);
            int playerCoordsY = (int) Math.floor(playerStates.get(0).getY()/20);
            path = pathFinder((int) Math.floor(x/20), (int) Math.floor(y/20), playerCoordsX, playerCoordsY, map.getMap());

        }
    }

    private void moveTile(){

    }

    private Path pathFinder(int sx, int sy, int tx, int ty, String[][] map){
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
                    if (i == 0 && j == 0){
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
