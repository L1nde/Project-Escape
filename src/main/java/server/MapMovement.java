package server;

import java.util.ArrayList;
import java.util.List;

public class MapMovement {
    private MapPoint end;
    private MapMovement prev;

    public MapMovement(MapPoint end, MapMovement prev) {
        this.end = end;
        this.prev = prev;
    }

    public List<MapPoint> getPath(){
        List res;
        if(prev == null){
            res = new ArrayList<>();
        } else {
            res = prev.getPath();
        }
        res.add(end);
        return res;
    }

    public MapPoint getEnd() {
        return end;
    }
}
