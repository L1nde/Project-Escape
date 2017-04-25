package general;

import server.ServerMazeMap;

import java.io.Serializable;
import java.util.HashMap;

public class PlayerState implements Serializable {
    private float x;
    private float y;
    private double dx;
    private double dy;
    private float speed;
    private int score;
    private int lives = 3;

    private PlayerInputState input;
    public PlayerState(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        input = new PlayerInputState();
    }

    public void reset(){
        // TODO game over
        if (lives != 0){
            x = 100;
            y = 100;
            score = 0;
            lives--;

        }


    }

    public int[][] getPosTiles(double dx, double dy) {
        int[][] posTiles = new int[4][2];

        int x0 = (int) Math.floor((x + dx)/20.0);
        int y0 = (int) Math.floor((y + dy)/20.0);
        posTiles[0] = new int[]{x0, y0};
        x0 = (int) Math.floor((x + 18 + dx)/20.0);
        y0 = (int) Math.floor((y + dy)/20.0);
        posTiles[1] = new int[]{x0, y0};
        x0 = (int) Math.floor((x + dx)/20.0);
        y0 = (int) Math.floor((y + 18 + dy)/20.0);
        posTiles[2] = new int[]{x0, y0};
        x0 = (int) Math.floor((x + 18 + dx)/20.0);
        y0 = (int) Math.floor((y + 18 + dy)/20.0);
        posTiles[3] = new int[]{x0, y0};

        return posTiles;
    }

    public PlayerInputState getInput() {
        return input;
    }

    public void setInput(PlayerInputState input) {
        this.input = input;
    }

    public void calculateNewPos(float timeDelta, ServerMazeMap map){
        //input may be sabotaged
        if(input.isMoving() && Double.isFinite(input.getAccelerationDirection())){
            dx = speed * timeDelta * Math.cos(input.getAccelerationDirection());
            dy = speed * timeDelta * Math.sin(input.getAccelerationDirection());
            int[][] newPosTilesY = getPosTiles(0, dy);
            int[][] newPosTilesX = getPosTiles(dx, 0);
            String[][] smap = map.getMap();
            //collision
            //up
            float x1 = x + 10;
            float y1 = y + 10;
            if (dy < 0 && !(smap[newPosTilesY[0][0]][newPosTilesY[0][1]].equals("W") || smap[newPosTilesY[1][0]][newPosTilesY[1][1]].equals("W"))){
                y += dy;
                if(smap[(int) Math.floor((x1)/20.0)][(int) Math.floor((y+8)/20.0)].equals("F")) {
                    smap[(int) Math.floor((x1)/20.0)][(int) Math.floor((y+8)/20.0)] = "P";
                    map.setMapUpdate(new MapUpdate((int) Math.floor((x1)/20.0),(int) Math.floor((y+8)/20.0), "P"));
                    score++;
                }
            //down
            } else if (dy > 0 && !(smap[newPosTilesY[2][0]][newPosTilesY[2][1]].equals("W") || smap[newPosTilesY[3][0]][newPosTilesY[3][1]].equals("W"))){
                y += dy;
                if(smap[(int) Math.floor((x1)/20.0)][(int) Math.floor((y+12)/20.0)].equals("F")) {
                    smap[(int) Math.floor((x1)/20.0)][(int) Math.floor((y+12)/20.0)] = "P";
                    map.setMapUpdate(new MapUpdate((int) Math.floor((x1)/20.0),(int) Math.floor((y+12)/20.0), "P"));
                    score++;
                }
            //right
            } if (dx < 0 && !(smap[newPosTilesX[0][0]][newPosTilesX[0][1]].equals("W") || smap[newPosTilesX[2][0]][newPosTilesX[2][1]].equals("W"))){
                x += dx;
                if(smap[(int) Math.floor((x+8)/20.0)][(int) Math.floor((y1)/20.0)].equals("F")) {
                    smap[(int) Math.floor((x+8)/20.0)][(int) Math.floor((y1)/20.0)] = "P";
                    map.setMapUpdate(new MapUpdate((int) Math.floor((x+5)/20.0),(int) Math.floor((y1)/20.0), "P"));
                    score++;
                }
            //left
            } else if (dx > 0 && !(smap[newPosTilesX[1][0]][newPosTilesX[1][1]].equals("W") || smap[newPosTilesX[3][0]][newPosTilesX[3][1]].equals("W"))){
                x += dx;
                if(smap[(int) Math.floor((x+12)/20.0)][(int) Math.floor((y1)/20.0)].equals("F")) {
                    smap[(int) Math.floor((x+12)/20.0)][(int) Math.floor((y1)/20.0)] = "P";
                    map.setMapUpdate(new MapUpdate((int) Math.floor((x+12)/20.0),(int) Math.floor((y1)/20.0), "P"));
                    score++;
                }
            }
            // TODO accelerated movement
        }
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public double getdX() {
        return dx;
    }

    public double getdY() {
        return dy;
    }
}