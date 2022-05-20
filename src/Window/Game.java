package Window;

import AIresorces.NeuralNetwork;

import javax.swing.JPanel;


import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.*;
import java.awt.event.*;

public class Game extends JPanel implements ActionListener {
    public final static int PANEL_WIDTH = 1000;
    public final static int PANEL_HEIGHT = 500;
    public final static int Cell = 20;
    public final static int TotalCells = (PANEL_WIDTH * PANEL_HEIGHT) / (Cell * Cell * 2);
    Coordinate[] cords = new Coordinate[TotalCells];
    NeuralNetwork brain;
    List<Coordinate[]> allSnakePositions;
    Coordinate apple;
    //int xApple;
    //int yApple;
    int Score;
    int direction = 3;
    int bodyValue = 3;
    int population = 300;
    boolean humanplayer = true;
    boolean seeVision = true;
    boolean replay = true;
    boolean running = false;
    boolean simulation = true;
    boolean replayBest = true;
    double[] vision;
    List<Double> decisions;
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
        placeApple();
        running = true;
        initSnake();
        if(simulation)
            simulate();

        timer = new Timer(75, this);
        timer.start();

    }

    private void simulate() {

        brain = new NeuralNetwork(24,20,4,0.04);
        snakeVision();
        snakeThink();
    }

    private void snakeThink() {
        double max;
        decisions=new ArrayList<>(4);
        decisions.addAll(brain.predict(vision));
        max= (decisions.get(0)>decisions.get(1))?decisions.get(0):decisions.get(1);
        max= (max>decisions.get(2))?max:decisions.get(2);
        max= (max>decisions.get(3))?max:decisions.get(3);


        direction = (decisions.indexOf(max)+1);
        System.out.println(direction);
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
        cords[0].setX(Cell*10);
        cords[0].setY(Cell*10);
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
                g.fillRect(cords[i].getX() + 500, cords[i].getY(), Cell, Cell);
            } else {

                g.setColor(new Color(40, 160, 0));
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
        g.fillOval(apple.getX() + 500, apple.getY(), Cell, Cell);
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
                if(wallCollide(x,y))
                    return;
                g.drawOval(x,y, cSize, cSize);
                i++;

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
        apple= new Coordinate((random.nextInt((int) (PANEL_WIDTH / (2 * Cell))) * Cell),random.nextInt((int) (PANEL_HEIGHT / Cell)) * Cell);
        //xApple = (random.nextInt((int) (PANEL_WIDTH / (2 * Cell))) * Cell);
        //yApple = random.nextInt((int) (PANEL_HEIGHT / Cell)) * Cell;
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

        }
        switch (direction) {
            case 1: // left
                cords[0].setX(cords[0].getX() - Cell);
                break;
            case 2: // up
                cords[0].setY(cords[0].getY() - Cell);
                break;

            case 3: // right
                cords[0].setX(cords[0].getX() + Cell);
                break;
            case 4: // down
                cords[0].setY(cords[0].getY() + Cell);
                break;
        }
    }

    public void checkCollision() {
        if (wallCollide(cords[0].getX() + 500, cords[0].getY()) || bodyCollide(cords[0].getX() + 500, cords[0].getY())) {
            running = false;
            System.out.println("sadasda"+cords[0].getX());
        }
        if (!running) {
            timer.stop();
        }
    }

    private boolean bodyCollide(int x, int y) {
        for (int i = bodyValue; i > 0; i--) {
            if ((x == cords[i].getX() + 500) && (y == cords[i].getY()))
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
        if (foodCollide(cords[0].getX() + 500, cords[0].getY())) {
            bodyValue++;
            Score++;
            placeApple();
        }
    }
    private boolean foodCollide(int xVector, int yVector) {
        if (xVector == apple.getX() + 500 && yVector == apple.getY()) {
            return true;
        }
        return false;
    }
}
