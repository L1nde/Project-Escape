package general;

import server.ServerMazeMap;

import java.io.Serializable;

public class PlayerState implements Serializable {
    private float x;
    private float y;
    private float speed;
    private PlayerInputState input;

    public PlayerState(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        input = new PlayerInputState();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
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
            double dx = speed * timeDelta * Math.cos(input.getAccelerationDirection());
            double dy = speed * timeDelta * Math.sin(input.getAccelerationDirection());
            int[][] newPosTilesY = getPosTiles(0, dy);
            int[][] newPosTilesX = getPosTiles(dx, 0);
            String[][] smap = map.getMap();

            if (dy < 0 && smap[newPosTilesY[0][0]][newPosTilesY[0][1]] == null && smap[newPosTilesY[1][0]][newPosTilesY[1][1]] == null){
                y += dy;

            } else if (dy > 0 && smap[newPosTilesY[2][0]][newPosTilesY[2][1]] == null && smap[newPosTilesY[3][0]][newPosTilesY[3][1]] == null){
                y += dy;

            } if (dx < 0 && smap[newPosTilesX[0][0]][newPosTilesX[0][1]] == null && smap[newPosTilesX[2][0]][newPosTilesX[2][1]] == null){
                x += dx;

            } else if (dx > 0 && smap[newPosTilesX[1][0]][newPosTilesX[1][1]] == null && smap[newPosTilesX[3][0]][newPosTilesX[3][1]] == null){
                x += dx;

            }
            // TODO accelerated movement
        }
    }
}
