package general;

import java.io.Serializable;

public class PlayerInputState implements Serializable{
    private float accelerationDirection;
    private boolean moving;

    public PlayerInputState() { }

    public PlayerInputState(float accelerationDirection) {
        this.accelerationDirection = accelerationDirection;
        this.moving = true;
    }

    public boolean isMoving() {
        return moving;
    }

    public float getAccelerationDirection() {
        return accelerationDirection;
    }
}
