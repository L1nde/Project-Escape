package general;

import java.io.Serializable;

public class PlayerState implements Serializable {
    private Point loc;
    private double speed;
    private int lives;
    private int score;
    private double movementDir;
    private PlayerInputState input;

    public PlayerState(Point loc, double speed, int lives, int score, double movementDir, PlayerInputState input) {
        this.loc = loc;
        this.speed = speed;
        this.lives = lives;
        this.score = score;
        this.movementDir = movementDir;
        this.input = input;
    }

    public PlayerInputState getInput() {
        return input;
    }

    public double getMovementDir(){
        return movementDir;
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