package server.Ghosts;

import general.GhostState;
import general.Point;
import server.ServerMazeMap;
import server.ServerTicker;

import java.util.List;

public class GhostMoveRandom implements GhostObjects{
    private float speed;
    private Point location;
    private List<Point> path;
    public GhostMoveRandom(float x, float y, float speed) {
        location = new Point(x, y);
        this.speed = speed;
    }
    @Override
    public void calculateNewPos(double timeDelta, ServerMazeMap map) {
        while(timeDelta > ServerTicker.EPS){
            if(path.isEmpty()){
                path = map.findShortestPath(location, map.findRandomValidPoint(location, 1000));
            }
            if(!path.isEmpty()){
                float distNxt = location.distance(path.get(0));
                if(distNxt < ServerTicker.EPS){
                    path.remove(0);
                } else {
                    float distDelta = (float) Math.min(distNxt, speed*timeDelta);
                    timeDelta -= distDelta/speed;
                    location = location.moveTowards(path.get(0), distDelta);
                }
            }
        }
    }

    @Override
    public float getX() {
        return location.getX();
    }

    @Override
    public float getY() {
        return location.getY();
    }

    @Override
    public GhostState getAsState() {
        return new GhostState(location);
    }
}
