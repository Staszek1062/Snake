package Window;
import javax.swing.*;
public class Frame extends JFrame {
   Game panel;

    public Frame(){
        panel =new Game();
        this.setResizable(false);
        this.setTitle("Snake Game?");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

}
