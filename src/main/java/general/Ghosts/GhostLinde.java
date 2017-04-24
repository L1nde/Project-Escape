package general.Ghosts;/*
 * Created by L1ND3 on 06.04.2017. 
 */


import general.PlayerState;
import org.newdawn.slick.util.pathfinding.Path;
import server.ServerMazeMap;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GhostLinde {
    private Map<Integer, Path> paths = new HashMap<>();

    public GhostLinde() {
    }

    public void calculateNewPos(ServerMazeMap map, Map<Integer, PlayerState> playerStates, GhostObject ghost, int id) {
        paths.putIfAbsent(id, null);
        Path path = paths.get(id);
        float x = ghost.getX();
        float y = ghost.getY();
        boolean moving = ghost.isMoving();
        Path.Step step;
        int count = ghost.getCount();
        if (playerStates.size() != 0){
            int playerCoordsX = (int) Math.floor(playerStates.get((int)(Math.random()*playerStates.size())).getX()/20);
            int playerCoordsY = (int) Math.floor(playerStates.get((int)(Math.random()*playerStates.size())).getY()/20);
            for (PlayerState playerState : playerStates.values()) {
                if ((int) Math.floor(playerState.getX()/20) == Math.floor(x/20) && (int) Math.floor(playerState.getY()/20) == Math.floor(y/20))
                    playerState.reset();
            }

            if (path == null){
                // Todo Try merging with pathfinder class
                ExecutorService pool = Executors.newFixedThreadPool(1);
                Future<Path> submit;
                try {
                    submit = pool.submit(new PathFinderThread((int)Math.floor(x/20), (int)Math.floor(y/20), playerCoordsX, playerCoordsY, map));
                } finally {
                    pool.shutdown();
                }
                try {
                    path = submit.get();
                    paths.replace(id, path);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (count == path.getLength()-1 && !moving){
                    paths.replace(id, null);
                    ghost.setCount(0);
                }
                if (!moving){
                    ghost.setMoving(true);
                    if (count < path.getLength()-1) {
                        ghost.setCount(count+1);
                    }
                }
                step = path.getStep(ghost.getCount());
                moveTile(step, x, y, ghost);
            }
        }
    }

    private void moveTile(Path.Step step, float x, float y, GhostObject ghost){
//        System.out.println(step.getX() + " " + step.getY() + " " + Math.floor(x/20) + " " + Math.floor(y/20));
        if (step.getX()*20 == (int)x && step.getY()*20 == (int)y){
            ghost.setMoving(false);
        }
        int muutX = (step.getX()*20-x) > 0 ? 1 : -1;
        muutX = (step.getX()*20-x) == 0 ? 0 : muutX;
        int muutY = (step.getY()*20-y) > 0 ? 1 : -1;
        muutY = (step.getY()*20-y) == 0 ? 0 : muutY;
        if (muutX != 0 && muutY == 0){
            ghost.setX(x + muutX);

        }
        if (muutY != 0 && muutX == 0){
            ghost.setY(y + muutY);

        }
    }


}
