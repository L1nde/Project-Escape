package general;

import java.io.Serializable;

public class PlayerInputState implements Serializable{
    private double accelerationDirection;
    private boolean moving;

    public PlayerInputState() { }

    public PlayerInputState(double accelerationDirection) {
        this.accelerationDirection = accelerationDirection;
        this.moving = true;
    }

    public void fixFormating(){
        //sabotage protection
        if(moving && Double.isFinite(accelerationDirection)){
            accelerationDirection = accelerationDirection % (2*Math.PI);
        } else {
            moving = false;
            accelerationDirection = 0;
        }

    }

    public boolean isMoving() {
        return moving;
    }

    public double getAccelerationDirection() {
        return accelerationDirection;
    }
}
