package general;

import java.io.Serializable;

public class PlayerState implements Serializable {
    private Point loc;
    private double speed;
    private int lives;
    private int score;
    private PlayerInputState input;

    public PlayerState(Point loc, double speed, int lives, int score, PlayerInputState input) {
        this.loc = loc;
        this.speed = speed;
        this.lives = lives;
        this.score = score;
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

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }
}