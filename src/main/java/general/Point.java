package general;

public class Point {
    private float x, y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public MapPoint getMapPoint() {
        return new MapPoint((int)(x/20), (int)(y/20));
    }

    public float distance(Point to){
        return (float)Math.sqrt((to.x - x)*(to.x - x) + (to.y - y)*(to.y - y));
    }

    public Point moveTowards(Point to, float dist){
        float totDist = distance(to);
        float dx = dist * (Math.abs(to.x - x)/totDist);
        float dy = dist * (Math.abs(to.y - y)/totDist);
        return new Point(x + dx, y + dy);
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

}
