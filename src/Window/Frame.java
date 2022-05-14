package Window;
import javax.swing.*;
public class Frame extends JFrame {
   Game game;

    public Frame(){
        game =new Game();
        this.setResizable(false);
        this.setTitle("Snake Game?");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(game);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

}
