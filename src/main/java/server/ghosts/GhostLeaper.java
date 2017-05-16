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
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Meelis Perli on 5/16/2017.
 */
public class GhostLeaper implements Ghost {
    private double speed;
    private Point loc;
    private final int rushMaxDistance = 50;
    private boolean special = false;
    private ServerMazeMap map;
    private ServerGameState gameState;
    private List<Point> path = new ArrayList<>();
    private static final double chaseLambda = 0.017; //Smaller values cause more chasing
    private static final double maxRandMoveDist = 8;
    private final double sideLen = 1;
    private final int leapMaxRange = 2;
    private final GhostType type = GhostType.LEAPER;

    public GhostLeaper(double x, double y, double speed, ServerMazeMap map, ServerGameState gameState) {
        loc = new Point(x, y);
        loc = map.findRandomValidPoint(loc, maxRandMoveDist /2);
        loc = new MapPoint(loc).getPoint();
        this.map = map;
        this.gameState = gameState;
        this.speed = speed;
    }

    @Override
    public void calculateNewPos(double timeDelta) {
        while(timeDelta > ServerTicker.EPS){
            if(path.isEmpty()){
                loc = leap();
                double maxChaserange = getNextExpDistr(chaseLambda);
                Point closestPlayerLoc = gameState.getClosestPlayerLoc(loc);
                Point dest;
                if(closestPlayerLoc != null && loc.distance(closestPlayerLoc) <= maxChaserange){
                    dest = closestPlayerLoc;
                } else {
                    dest = map.findRandomValidPoint(loc, maxRandMoveDist);
                }
                dest = new MapPoint(dest).getPoint();
                path = map.findShortestPath(loc, dest);
            }
            if(!path.isEmpty()){
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
    public Point getLoc() {
        return loc;
    }

    @Override
    public double getSideLen() {
        return sideLen;
    }

    @Override
    public GhostState getAsState() {
        return new GhostState(loc, type, special);
    }
    private double getNextExpDistr(double lambda) {
        return Math.log(1- ThreadLocalRandom.current().nextDouble())/(-lambda);
    }

    @Override
    public GhostType getGhostType() {
        return type;
    }

    private Point leap() {
        MapPoint location1 = new MapPoint(loc);
        List<Point> possibleLocations = new ArrayList<>();
        for (int x = -leapMaxRange; x < leapMaxRange + 1; x++) {
            for (int y = -leapMaxRange; y < leapMaxRange + 1; y++) {
                if (map.getTile(new MapPoint(location1.getX() + x, location1.getY() + y)) != TileType.WALL && x != 0 && y != 0) {
                    possibleLocations.add(new Point(location1.getX() + x + 0.5, location1.getY() + y + 0.5));
                }
            }
        }
        return possibleLocations.get((int)(Math.random()*possibleLocations.size()));
    }
}
