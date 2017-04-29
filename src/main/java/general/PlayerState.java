package general;

import java.io.Serializable;

public class PlayerState implements Serializable {
    private Point loc;
    private double speed;
    private PlayerInputState input;

    public PlayerState(Point loc, double speed, PlayerInputState input) {
        this.loc = loc;
        this.speed = speed;
        this.input = input;
    }

    public PlayerInputState getInput() {
        return input;
    }

    public double getMovementDir(){
        return input.getAccelerationDirection();
    }

    public Point getLoc() {
        return loc;
    }
}