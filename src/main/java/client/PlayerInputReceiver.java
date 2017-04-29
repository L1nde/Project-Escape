package client;

import general.PlayerInputState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class PlayerInputReceiver {
    public static PlayerInputState receive(GameContainer gc) {
        Input input = gc.getInput();
        int dx=0, dy=0;
        if (input.isKeyDown(Input.KEY_UP) || (input.isKeyDown(Input.KEY_W))) {
            --dy;
        }
        if (input.isKeyDown(Input.KEY_DOWN) || (input.isKeyDown(Input.KEY_S))) {
            ++dy;
        }
        if (input.isKeyDown(Input.KEY_LEFT) || (input.isKeyDown(Input.KEY_A))) {
            --dx;
        }
        if (input.isKeyDown(Input.KEY_RIGHT) || (input.isKeyDown(Input.KEY_D))) {
            ++dx;
        }
        if(dx == 0 && dy == 0){
            return new PlayerInputState();
        }
        else{
            return new PlayerInputState(Math.atan2(dy, dx));
        }
    }

}
