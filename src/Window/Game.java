package Window;

import javax.swing.JPanel;
import java.awt.*;
import java.util.Random;

import javax.swing.*;
import java.awt.event.*;

public class Game extends JPanel implements ActionListener {
    public final static int PANEL_WIDTH = 1000;
    public final static int PANEL_HEIGHT = 500;
    public final static int Cell = 20;
    public final static int TotalCells = (PANEL_WIDTH * PANEL_HEIGHT) / (Cell * Cell * 2);
    protected final int xCellGrid[] = new int[TotalCells];
    protected final int yCellGrid[] = new int[TotalCells];

    int xApple;
    int yApple;
    int Score;
    int direction = 3;
    int bodyValue = 3;
    int population = 300;
    boolean humanplayer = true;
    boolean seeVision = true;
    boolean replay = false;
    boolean running = false;
    boolean simulation = false;
    boolean replayBest = true;

    Timer timer;
    Random random;
    Image backgroundImage;

    public Game() {
        random = new Random();
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        backgroundImage = new ImageIcon("src/Window/Background_grass.jpg").getImage();
        this.addKeyListener(new ControlWithKeys(this));
        run();
    }

    public void run() {
        running = true;
        placeApple();
        timer = new Timer(75, this);
        timer.start();
    }

    public void draw(Graphics g) {
        if (running) {
            g.drawImage(backgroundImage, 500, 0, null);

            drawBoard(g);
            drawApple(g);
            drawSnake(g);
            if (replay && seeVision) { // drawing snake vision
                drawVision(g);
            }
            drawScoreboard(g);
        } else {
            gameOver(g);
        }
    }

    private void drawSnake(Graphics g) {
        for (int i = 0; i < bodyValue; i++) { // drawing Snake
            if (i == 0) {
                g.setColor(Color.green);
                g.fillRect(xCellGrid[i] + 500, yCellGrid[i], Cell, Cell);
            } else {
                g.setColor(new Color(40, 160, 0));
                g.fillRect(xCellGrid[i] + 500, yCellGrid[i], Cell, Cell);
            }
        }
    }

    private void drawBoard(Graphics g) {
        for (int i = 0; i < PANEL_HEIGHT / Cell; i++) { // drawing of snake board lines
            g.drawLine(500 + i * Cell, 0, 500 + i * Cell, PANEL_HEIGHT);
            g.drawLine(500, i * Cell, PANEL_WIDTH, i * Cell);
        }
    }

    private void drawApple(Graphics g) {
        g.setColor(Color.red);// drawing apple
        g.fillOval(xApple + 500, yApple, Cell, Cell);
    }

    private void drawVision(Graphics g) {
        for (int i = 0; i < PANEL_HEIGHT / Cell; i++) {
            int var = Cell * i;

            g.drawOval(xCellGrid[0] + 507, var + 7, 5, 5);
            g.drawOval(var + 507, yCellGrid[0] + 7, 5, 5);
            if ((xCellGrid[0] + 500 + var) < PANEL_WIDTH && (yCellGrid[0] + var) < PANEL_HEIGHT) {
                g.drawOval(xCellGrid[0] + 507 + var, yCellGrid[0] + var + 7, 5, 5);

            }
            if ((xCellGrid[0] + 507 - var) > 500 && (yCellGrid[0] + var + 7) < PANEL_HEIGHT) {
                g.drawOval(xCellGrid[0] + 507 - var, yCellGrid[0] + var + 7, 5, 5);
            }

            if ((xCellGrid[0] + 507 - var > 500) && (yCellGrid[0] - var + 7 > 0)) {
                g.drawOval(xCellGrid[0] + 507 - var, yCellGrid[0] - var + 7, 5, 5);
            }
            if ((xCellGrid[0] + 507 + var < PANEL_WIDTH) && (yCellGrid[0] - var + 7 > 0)) {
                g.drawOval(xCellGrid[0] + 507 + var, yCellGrid[0] - var + 7, 5, 5);
            }

        }
    }

    public void gameOver(Graphics g) {
        drawScoreboard(g);
        drawGameOver(g);
    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (PANEL_WIDTH - metrics2.stringWidth("Game Over")) / 2, PANEL_HEIGHT / 2);
    }

    private void drawScoreboard(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + Score, (PANEL_WIDTH - metrics1.stringWidth("Score: " + Score)) / 2,
                g.getFont().getSize());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && humanplayer) {
            Snakes();
        }
        repaint();
    }

    public void placeApple() {
        xApple = (random.nextInt((int) (PANEL_WIDTH / (2 * Cell))) * Cell);
        yApple = random.nextInt((int) (PANEL_HEIGHT / Cell)) * Cell;
    }

   

    public void Snakes() {
        move();
        checkCollision();
        checkFood();
    }

    public void move() {
        for (int i = bodyValue; i > 0; i--) {
            xCellGrid[i] = xCellGrid[i - 1];
            yCellGrid[i] = yCellGrid[i - 1];
        }
        switch (direction) {
            case 1: // left
                xCellGrid[0] = xCellGrid[0] - Cell;
                break;
            case 2: // up
                yCellGrid[0] = yCellGrid[0] - Cell;
                break;

            case 3: // right
                xCellGrid[0] = xCellGrid[0] + Cell;
                break;
            case 4: // down
                yCellGrid[0] = yCellGrid[0] + Cell;
                break;
        }
    }

    public void checkCollision() {
        if (wallCollide(xCellGrid[0] + 500, yCellGrid[0]) || bodyCollide(xCellGrid[0] + 500, yCellGrid[0])) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }

    private boolean bodyCollide(int x, int y) {
        for (int i = bodyValue; i > 0; i--) {
            if ((x == xCellGrid[i] + 500) && (y == yCellGrid[i]))

                return true;
        }
        return false;
    }

    private boolean wallCollide(int x, int y) {
        if (x >= PANEL_WIDTH || x < 500 || y >= PANEL_HEIGHT || y < 0) {

            return true;

        }
        return false;
    }

    public void checkFood() {
        if (foodCollide(xCellGrid[0] + 500, yCellGrid[0])) {
            bodyValue++;
            Score++;
            placeApple();
        }

    }

    private boolean foodCollide(int xVector, int yVector) {
        if (xVector == xApple + 500 && yVector == yApple) {
            return true;
        }
        return false;
    }
    

}
