package general;

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

    public PlayerInputState getInput() {
        return input;
    }

    public void setInput(PlayerInputState input) {
        this.input = input;
    }


    public void calculateNewPos(float timeDelta){
        //input may be sabotaged
        if(input.isMoving() && Double.isFinite(input.getAccelerationDirection())){
            x += speed * timeDelta * Math.cos(input.getAccelerationDirection());
            y += speed * timeDelta * Math.sin(input.getAccelerationDirection());
            // TODO accelerated movement
        }
    }
}
