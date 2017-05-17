package server.ghosts;

import general.GhostState;
import general.GhostType;
import general.Point;
import server.MapPoint;
import server.ServerGameState;
import server.ServerMazeMap;
import server.ServerTicker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GhostMoveRandom implements Ghost {
    private double speed;
    private Point loc;
    private List<Point> path = new ArrayList<>();
    private double sideLen = 1;
    private ServerMazeMap map;
    private ServerGameState gameState;
    private static final double chaseLambda = 0.017; //Smaller values cause more chasing
    private static final double maxRandMoveDist = 8;
    private final GhostType type = GhostType.NORMAL;

    public GhostMoveRandom(double x, double y, double speed, ServerMazeMap map, ServerGameState gameState) {
        loc = new Point(x, y);
        loc = map.findRandomValidPoint(loc, maxRandMoveDist /2);
        loc = new MapPoint(loc).getPoint();
        this.speed = speed;
        this.map = map;
        this.gameState = gameState;
    }

    /**
     * Repeatedly selects a random free tile centre in some range
     * and moves toward it along the shortest path.
     */
    @Override
    public void calculateNewPos(double timeDelta) {

        while(timeDelta > ServerTicker.EPS){
            if(path.isEmpty()){
                double maxChaseRange = getNextExpDistr(chaseLambda);
                Point closestPlayerLoc = gameState.getClosestPlayerLoc(loc);
                Point dest;
                if(closestPlayerLoc != null && loc.distance(closestPlayerLoc) <= maxChaseRange){
                    dest = closestPlayerLoc;
                } else {
                    dest = map.findRandomValidPoint(loc, maxRandMoveDist);
                }
                dest = new MapPoint(dest).getPoint();
                path = map.findShortestPath(loc, dest);
            } else {
                double distNxt = loc.distance(path.get(0));
                if(distNxt < ServerTicker.EPS){
                    path.remove(0);
                } else {
                    double distDelta = Math.min(distNxt, speed*timeDelta);
                    timeDelta -= distDelta/speed;
                    loc = loc.moveTowards(path.get(0), distDelta);
                }
            }
        }
    }

    @Override
    public void reset(){
        loc = map.findRandomValidPoint(new Point(20, 15), 15);
        path.clear();
    }


    @Override
    public Point getLoc() {
        return loc;
    }

    @Override
    public GhostState getAsState() {
        return new GhostState(loc, type, false);
    }

    @Override
    public GhostType getGhostType() {
        return type;
    }

    @Override
    public double getSideLen() {
        return sideLen;
    }
    private double getNextExpDistr(double lambda) {
        return Math.log(1- ThreadLocalRandom.current().nextDouble())/(-lambda);
    }
}
