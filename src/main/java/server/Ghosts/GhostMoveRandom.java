package server.Ghosts;

import general.GhostState;
import general.Point;
import server.MapPoint;
import server.ServerMazeMap;
import server.ServerTicker;

import java.util.ArrayList;
import java.util.List;

public class GhostMoveRandom implements Ghost {
    private double speed;
    private Point loc;
    private List<Point> path = new ArrayList<>();
    private ServerMazeMap map;

    public GhostMoveRandom(double x, double y, double speed, ServerMazeMap map) {
        loc = new Point(x, y);
        loc = map.findRandomValidPoint(loc, 100);
        loc = new MapPoint(loc).getPoint();
        this.speed = speed;
        this.map = map;
    }

    /**
     * Repeatedly selects a random free centrepoint in some range
     * and moves toward it along the shortest path.
     */
    @Override
    public void calculateNewPos(double timeDelta) {
        while(timeDelta > ServerTicker.EPS){
            if(path.isEmpty()){
                Point dest = map.findRandomValidPoint(loc, 200);
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
    public GhostState getAsState() {
        return new GhostState(loc);
    }
}
