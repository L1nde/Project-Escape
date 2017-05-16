package server;

import general.*;
import server.ghosts.Ghost;
import server.ghosts.GhostHungry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Player {
    private final Point startLoc;
    private final Point deadLoc = new Point(-100, -100);
    private Point loc;
    private double speed;
    private int lives;
    private int score = 0;
    private PlayerInputState input;
    private ServerMazeMap map;
    private double movementDir = 0;
    private static int iter = 0;
    private double sideLen = 18;

    public Player(Point loc, double speed, int lives, ServerMazeMap map) {
        this.startLoc = loc;
        this.loc = loc;
        this.speed = speed;
        this.lives = lives;
        input = new PlayerInputState();
        this.map = map;
    }

    public PlayerInputState getInput() {
        return input;
    }

    public void setInput(PlayerInputState input) {
        this.input = input;
    }

    public double getMovementDir(){
        return input.getAccelerationDirection();
    }

    public Point getLoc() {
        return loc;
    }

    public void calculateNewPos(double timeDelta){
        ++iter;
        if(input.isMoving()){
            Point prevLoc = loc;
            while(timeDelta > ServerTicker.EPS){
                double dx = speed * timeDelta * Math.cos(input.getAccelerationDirection());
                double dy = speed * timeDelta * Math.sin(input.getAccelerationDirection());
                if(map.getFreeXRange(loc, dx) != 0.0 && map.getFreeYRange(loc, dy) != 0.0){
                    double cdx, cdy;
                    //Direction prioritization to enter door like places easier.
                    //Moving in both directions at once may move into wall.
                    boolean bias = Math.abs(dx) > Math.abs(dy);
                    //Use random to be unbiased on diagonal movement.
                    if(Math.abs(Math.abs(dx/dy) - 1) < 0.05){
                        bias = ThreadLocalRandom.current().nextBoolean();
                    }
                    if(bias){
                        cdx = Math.copySign(Math.min(map.getFreeXRange(loc, dx), Math.abs(dx)), dx);
                        loc = new Point(loc.getX() + cdx, loc.getY());
                        cdy = Math.copySign(Math.min(map.getFreeYRange(loc, dy), Math.abs(dy)), dy);
                        loc = new Point(loc.getX(), loc.getY() + cdy);
                    } else {
                        cdy = Math.copySign(Math.min(map.getFreeYRange(loc, dy), Math.abs(dy)), dy);
                        loc = new Point(loc.getX(), loc.getY() + cdy);
                        cdx = Math.copySign(Math.min(map.getFreeXRange(loc, dx), Math.abs(dx)), dx);
                        loc = new Point(loc.getX() + cdx, loc.getY());
                    }
                    double distDelta = Math.sqrt(cdx*cdx + cdy*cdy);
                    timeDelta -= distDelta/speed;
                } else if(map.getFreeYRange(loc, dy) != 0.0){
                    MapPoint idx = new MapPoint(loc);
                    if(dx > 0){
                        idx = idx.right();
                    } else{
                        idx = idx.left();
                    }
                    boolean intoWall = Math.abs(dy/dx) < Math.tan(Math.PI/8);
                    dy = Math.copySign(speed * timeDelta, dy);
                    dx = 0;
                    //If there is a empty space near the blocked direction and
                    // movement direction is closely enough toward it,
                    // move parallely to reach it.
                    if(map.getTile(idx) != TileType.WALL && intoWall){
                        if(idx.getPoint().getY() > loc.getY()){
                            dy = Math.copySign(dy, 1);
                        } else {
                            dy = Math.copySign(dy, -1);
                        }
                    } else if(intoWall) { //To stop sideways motion when moving into wall
                        break;
                    }
                    if(map.getFreeYRange(loc, dy) == 0.0){
                        break;
                    }
                    double cdy = Math.copySign(Math.min(map.getFreeYRange(loc, dy), Math.abs(dy)), dy);
                    loc = new Point(loc.getX(), loc.getY() + cdy);
                    double distDelta = Math.abs(cdy);
                    timeDelta -= distDelta/speed;
                } else if(map.getFreeXRange(loc, dx) != 0.0){
                    MapPoint idx = new MapPoint(loc);
                    if(dy > 0){
                        idx = idx.up();
                    } else{
                        idx = idx.down();
                    }
                    boolean intoWall = Math.abs(dx/dy) < Math.tan(Math.PI/8);
                    dx = Math.copySign(speed * timeDelta, dx);
                    dy = 0;
                    //If there is a empty space near the blocked direction and
                    // movement direction is closely enough toward it,
                    // move parallely to reach it.
                    if(map.getTile(idx) != TileType.WALL && intoWall){
                        if(idx.getPoint().getX() > loc.getX()){
                            dx = Math.copySign(dx, 1);
                        } else {
                            dx = Math.copySign(dx, -1);
                        }
                    } else if(intoWall) { //To stop sideways motion when moving into wall
                        break;
                    }
                    if(map.getFreeXRange(loc, dx) == 0.0){
                        break;
                    }
                    double cdx = Math.copySign(Math.min(map.getFreeXRange(loc, dx), Math.abs(dx)), dx);
                    loc = new Point(loc.getX() + cdx, loc.getY());
                    double distDelta = Math.abs(cdx);
                    timeDelta -= distDelta/speed;
                } else {
                    timeDelta = 0;
                }
            }
            movementDir = Math.atan2(loc.getY()-prevLoc.getY(), loc.getX()-prevLoc.getX());
        } else {
            movementDir = 0;
        }
    }

    public void checkEntityCollisions(ServerGameState state){
        List<Ghost> ghostList = state.getCollidingGhosts(loc, 0.5);
        if(!ghostList.isEmpty()){
            for (Ghost ghost : ghostList) {
                System.out.println(ghost.getGhostType().toString());
                if (ghost.getGhostType() == GhostType.HUNGRY) {
                    state.addGhost(state.getGhostCount(), new GhostHungry(loc.getX(), loc.getY(), speed, map, state));
                    break;
                }
            }
            if(lives > 0){
                --lives;
            }
            if(lives > 0){
                loc = startLoc;
            } else {
                loc = deadLoc;
            }

        }
    }
    public List<MapUpdate> getMapUpdates(){
        List<MapUpdate> res = new ArrayList<>();
        MapPoint idx = new MapPoint(loc);
        if(map.getTile(idx) == TileType.FOOD){
            ++score;
            res.add(new MapUpdate(idx.getX(), idx.getY(), TileType.EMPTY));
        }
        return res;
    }

    public PlayerState getAsState(){
        return new PlayerState(loc, speed, lives, score, movementDir, input);
    }

    public Boolean isAlive(){
        return lives > 0;
    }

    public double getSideLen() {
        return sideLen;
    }
}
