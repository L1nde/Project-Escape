package general.Ghosts;/*
 * Created by L1ND3 on 06.04.2017. 
 */


import general.PlayerState;
import org.newdawn.slick.util.pathfinding.Path;
import server.ServerMazeMap;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GhostLinde implements GhostObjects, Serializable {
    private float x;
    private float y;
    private float speed;
    private List<Tile> open = new ArrayList<>();
    private List<Tile> closed = new ArrayList<>();
    private List<String> tiles = new ArrayList<>();
    private String[][] map;
    private boolean moving = false;
    private Path path = null;
    private int count = 0;
    private Path.Step step;


    public GhostLinde(float x, float y, float speed, ServerMazeMap smap) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.map = smap.getMap();

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
//        if (path == null){
//            int playerCoordsX = (int) Math.floor(playerStates.get(0).getX()/20);
//            int playerCoordsY = (int) Math.floor(playerStates.get(0).getY()/20);
//            ExecutorService pool = Executors.newFixedThreadPool(1);
//            Future<Path> submit;
//            try {
//                submit = pool.submit(new PathFinderThread((int)Math.floor(x/20), (int)Math.floor(y/20), playerCoordsX, playerCoordsY, this.map));
//            } finally {
//                pool.shutdown();
//            }
//            try {
//                path = submit.get();
//            } catch (InterruptedException | ExecutionException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        if (path != null){
//            if (!moving){
//                step = path.getStep(count);
//                count++;
//                moving = true;
//            }
//            if (count == path.getLength()){
//                path = null;
//                count = 0;
//            }
//            moveTile(step);
//        }
    }

    private void moveTile(Path.Step step){
        if (step.getX() == (int)Math.floor(x/20) && step.getY() == (int)Math.floor(y/20)){
            moving = false;
        }
//        System.out.println(step.getX());
//        System.out.println(step.getY());
        x += (step.getX()-(int)Math.floor(x/20))*0.5;
        y +=(step.getY()-(int)Math.floor(y/20))*0.5;
    }


}
