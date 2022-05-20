package Window;

import AIresorces.NeuralNetwork;

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
    Coordinate[] cords = new Coordinate[TotalCells];
    NeuralNetwork brain;
    int xApple;
    int yApple;
    int Score;
    int direction = 3;
    int bodyValue = 3;
    int population = 300;
    boolean humanplayer = true;
    boolean seeVision = true;
    boolean replay = true;
    boolean running = false;
    boolean simulation = false;
    boolean replayBest = true;
    double[] vision;

    Timer timer;
    Random random;


    public Game() {

        random = new Random();
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new ControlWithKeys(this));
        run();
    }

    public void run() {
        running = true;
        initSnake();
        if(simulation)
            simulate();
        placeApple();
        timer = new Timer(75, this);
        timer.start();

    }

    private void simulate() {


        //snakeVision();
        snakeThink();
    }

    private void snakeThink() {
        brain = new NeuralNetwork(24,20,4,0.04);
    }
    private void snakeVision() {
        vision = new double[24];
        double[] temp = checkVisionLine( Cell, Cell,cords[0]);
        vision[0] = temp[0];
        vision[1] = temp[1];
        vision[2] = temp[2];
        temp = checkVisionLine( Cell, 0,cords[0]);
        vision[3] = temp[0];
        vision[4] = temp[1];
        vision[5] = temp[2];
        temp = checkVisionLine( Cell, -Cell,cords[0]);
        vision[6] = temp[0];
        vision[7] = temp[1];
        vision[8] = temp[2];
        temp = checkVisionLine( 0, -Cell,cords[0]);
        vision[9] = temp[0];
        vision[10] = temp[1];
        vision[11] = temp[2];
        temp = checkVisionLine( -Cell, -Cell,cords[0]);
        vision[12] = temp[0];
        vision[13] = temp[1];
        vision[14] = temp[2];
        temp = checkVisionLine( -Cell, 0,cords[0]);
        vision[15] = temp[0];
        vision[16] = temp[1];
        vision[17] = temp[2];
        temp = checkVisionLine( -Cell, Cell,cords[0]);
        vision[18] = temp[0];
        vision[19] = temp[1];
        vision[20] = temp[2];
        temp = checkVisionLine( 0, Cell,cords[0]);
        vision[21] = temp[0];
        vision[22] = temp[1];
        vision[23] = temp[2];
    }
    private void initSnake() {
        for (int i = 0; i < TotalCells; i++)
            cords[i]= new Coordinate();
    }

    public void draw(Graphics g) {
        if (running) {
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
                // g.fillRect(xCellGrid[i] + 500, yCellGrid[i], Cell, Cell);
                g.fillRect(cords[i].getX() + 500, cords[i].getY(), Cell, Cell);
            } else {

                g.setColor(new Color(40, 160, 0));
                // g.fillRect(xCellGrid[i] + 500, yCellGrid[i], Cell, Cell);
                g.fillRect(cords[i].getX() + 500, cords[i].getY(), Cell, Cell);
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

    private void drawVision(Graphics g ) {

            drawVisionLine( Cell, Cell,cords[0],g);
            drawVisionLine( Cell, 0,cords[0], g);
            drawVisionLine( Cell, -Cell,cords[0], g);
            drawVisionLine( 0, -Cell,cords[0], g);

            drawVisionLine( -Cell, -Cell,cords[0], g);
            drawVisionLine( -Cell, 0,cords[0], g);
            drawVisionLine( -Cell, Cell,cords[0], g);
            drawVisionLine( 0, Cell,cords[0], g);


    }

        private void drawVisionLine(int xVector, int yVector, Coordinate cord, Graphics g) {
            int cSize = 6;
            int cPos = (Cell - cSize)/2;
            int i=1;
            Coordinate[] line;
            do{
                int x = cord.getX() + xVector * i + 500 + cPos;
                int y = cord.getY() + yVector * i + cPos;

                g.drawOval(x,y, cSize, cSize);
                i++;
                if(wallCollide(x,y))
                    return;
            }while(i<PANEL_HEIGHT/Cell);
    }
    private double[] checkVisionLine(int xVector, int yVector, Coordinate cord) {
            double[] results = new double[3];
            int i = 1;
        int x;
        int y;
        do{
            x = cord.getX() + xVector * i + 500 ;
            y = cord.getY() + yVector * i ;
            if(foodCollide(x,y))
                results[0]=1;
            if(bodyCollide(x,y))
                results[1]=1;
            i++;
        }while(!wallCollide(x,y));
            results[2]=1/i;
            return results;
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
        cords[bodyValue]= new Coordinate();
        for (int i = bodyValue; i > 0; i--) {
            cords[i].setX(cords[i - 1].getX());
            cords[i].setY(cords[i - 1].getY());
            xCellGrid[i] = xCellGrid[i - 1];
            yCellGrid[i] = yCellGrid[i - 1];
        }
        switch (direction) {
            case 1: // left
                xCellGrid[0] = xCellGrid[0] - Cell;
                cords[0].setX(cords[0].getX() - Cell);
                break;
            case 2: // up
                yCellGrid[0] = yCellGrid[0] - Cell;
                cords[0].setY(cords[0].getY() - Cell);
                break;

            case 3: // right
                xCellGrid[0] = xCellGrid[0] + Cell;
                cords[0].setX(cords[0].getX() + Cell);
                break;
            case 4: // down
                yCellGrid[0] = yCellGrid[0] + Cell;
                cords[0].setY(cords[0].getY() + Cell);
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
