package server.ghosts;

import general.GhostState;
import general.GhostType;
import general.Point;
import general.TileType;
import server.MapPoint;
import server.ServerGameState;
import server.ServerMazeMap;
import server.ServerTicker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Meelis Perli on 5/15/2017.
 */
public class GhostRusher implements Ghost{
    private double speed;
    private Point loc;
    private final int rushMaxDistance = 50;
    private boolean special = false;
    private final double sideLen = 1;
    private ServerMazeMap map;
    private ServerGameState gameState;
    private List<Point> path = new ArrayList<>();
    private static final double chaseLambda = 0.017; //Smaller values cause more chasing
    private static final double maxRandMoveDist = 8;
    private final int speedMultiplier = 5;
    private boolean stunned = false;
    private double stunTime = 0;
    private double maxStunTime = 40;
    private final GhostType type = GhostType.RUSHER;

    public GhostRusher(double x, double y, double speed, ServerMazeMap map, ServerGameState gameState) {
        loc = new Point(x, y);
        loc = map.findRandomValidPoint(loc, maxRandMoveDist /2);
        loc = new MapPoint(loc).getPoint();
        this.map = map;
        this.gameState = gameState;
        this.speed = speed/2;
    }

    @Override
    public GhostState getAsState() {
        return new GhostState(loc, type, special);
    }

    @Override
    public void calculateNewPos(double timeDelta) {
        while(timeDelta > ServerTicker.EPS && stunTime <= 0) {
            //uses GhostMoveRandom code, if special isn't activated.
            if (path.isEmpty()) {
                if (special) {
                    special = false;
                    stunned = true;
                    stunTime = maxStunTime;
                } else {
                    double maxChaseRange = getNextExpDistr(chaseLambda);
                    Point closestPlayerLoc = gameState.getClosestPlayerLoc(loc);
                    Point dest;
                    if (closestPlayerLoc != null && loc.distance(closestPlayerLoc) <= maxChaseRange) {
                        dest = closestPlayerLoc;
                    } else {
                        dest = map.findRandomValidPoint(loc, maxRandMoveDist);
                    }
                    dest = new MapPoint(dest).getPoint();
                    path = map.findShortestPath(loc, dest);
                }
            } else {
                //if ghost can see a player, then it's special will be activated
                if (!special) {
                    List<Point> playerLocs = gameState.getPlayerLocations();
                    //Check location in random order to avoid bias
                    Collections.shuffle(playerLocs, ThreadLocalRandom.current());
                    for (Point playerLoc : playerLocs) {
                        if (map.canSee(loc, playerLoc)) {
                            path = map.findShortestPath(loc, new MapPoint(getTargetLoc(playerLoc)).getPoint());
                            special = true;
                            break;
                        }
                    }
                }
                double curSpeed = speed;
                if(special){
                    curSpeed *= speedMultiplier;
                }
                double distNxt = loc.distance(path.get(0));
                if (distNxt < ServerTicker.EPS) {
                    path.remove(0);
                } else {
                    double distDelta = Math.min(distNxt, curSpeed * timeDelta);
                    timeDelta -= distDelta / (curSpeed);
                    loc = loc.moveTowards(path.get(0), distDelta);
                }
            }
        }
        if (stunned) {
            stunTime -= timeDelta;
        }
    }
    @Override
    public Point getLoc() {
        return loc;
    }

    @Override
    public void reset(){
        loc = map.findRandomValidPoint(new Point(20, 15), 15);
        path.clear();
    }

    @Override
    public double getSideLen() {
        return sideLen;
    }

    private Point getTargetLoc(Point playerLoc) {
        MapPoint location1 = new MapPoint(loc);
        MapPoint location2 = new MapPoint(playerLoc);
        int dx = Math.abs(location1.getX() - location2.getX());
        int dy = Math.abs(location1.getY() - location2.getY());

        if (dy == 0) {
            if(location1.getX() < location2.getX()) {
                for (int i = 0; i < rushMaxDistance; i++) {
                    if (map.getTile(new MapPoint(location1.getX() + i, location1.getY())) == TileType.WALL) {
                        return new Point(location1.getX() + i - 1, location1.getY());
                    }
                }
                return new Point(location1.getX() + rushMaxDistance - 1, location1.getY());
            } else  {
                for (int i = 0; i < rushMaxDistance; i++) {
                    if (map.getTile(new MapPoint(location1.getX() - i, location1.getY())) == TileType.WALL) {
                        return new Point(location1.getX() - i + 1, location1.getY());
                    }
                }
                return new Point(location1.getX() - rushMaxDistance + 1, location1.getY());
            }

        } else if (dx == 0) {
            if(location1.getY() < location2.getY()) {
                for (int i = 0; i < rushMaxDistance; i++) {
                    if (map.getTile(new MapPoint(location1.getX(), location1.getY() + i)) == TileType.WALL) {
                        return new Point(location1.getX(), location1.getY() + i - 1);
                    }
                }
                return new Point(location1.getX(), location1.getY() + rushMaxDistance - 1);
            } else  {
                for (int i = 0; i < rushMaxDistance; i++) {
                    if (map.getTile(new MapPoint(location1.getX(), location1.getY() - i)) == TileType.WALL) {
                        return new Point(location1.getX(), location1.getY() - i + 1);
                    }
                }
                return new Point(location1.getX(), location1.getY() - rushMaxDistance + 1);
            }
        }
        return null;
    }

    private double getNextExpDistr(double lambda) {
        return Math.log(1- ThreadLocalRandom.current().nextDouble())/(-lambda);
    }

    @Override
    public GhostType getGhostType() {
        return type;
    }
}
