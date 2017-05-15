package general;

import java.io.Serializable;

//Tracks centre of object
public class Point implements Serializable{
    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Point to){
        return Math.sqrt((to.x - x)*(to.x - x) + (to.y - y)*(to.y - y));
    }

    public Point moveTowards(Point to, double dist){
        double totDist = distance(to);
        double dx = dist * ((to.x - x)/totDist);
        double dy = dist * ((to.y - y)/totDist);
        return new Point(x + dx, y + dy);
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

}
