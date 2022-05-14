package Window;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ControlWithKeys extends KeyAdapter {

    /**
     *
     */
    private final Game game;

    /**
     * @param game
     */
    ControlWithKeys(Game game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (this.game.direction != 3) {
                    this.game.direction = 1;
                }
                break;
            case KeyEvent.VK_UP:
                if (this.game.direction != 4) {
                    this.game.direction = 2;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (this.game.direction != 1) {
                    this.game.direction = 3;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (this.game.direction != 2) {
                    this.game.direction = 4;
                }
                break;

        }
    }
}