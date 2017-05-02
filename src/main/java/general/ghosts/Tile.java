package general.ghosts;/*
 * Created by L1ND3 on 19.04.2017. 
 */


import java.io.Serializable;

public class Tile implements Comparable<Tile>, Serializable {
    private int x;
    private int y;
    private Tile parent = null;
    private int cost;

    private int depth;

    public Tile(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int setParent(Tile parent){
        depth = parent.depth;
        this.parent = parent;
        return depth;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public Tile getParent() {
        return parent;
    }

    @Override
    public int compareTo(Tile o) {
        return cost-o.cost;

    }
}
