package Window;

import javax.swing.JPanel;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.*;
import java.awt.event.*;

public class Game extends JPanel implements ActionListener {
    public final static int PANEL_WIDTH = 1000;
    public final static int PANEL_HEIGHT = 500;
    public final static int Cell = 20;
    public final static int TotalCells = (PANEL_WIDTH * PANEL_HEIGHT) / (Cell * Cell * 2);
    private final Collisions collisions = new Collisions(PANEL_HEIGHT,PANEL_WIDTH);
    Coordinate[] cords = new Coordinate[TotalCells];

    List<Coordinate[]> snakePath;
    Coordinate apple;
    List<Coordinate> foodList;
    int Score;
    int direction = 3;
    int bodyValue = 3;
    int population = 300;
    int step=0;

    boolean seeVision = true;
    boolean replay = true;
    boolean running = false;
    boolean simulation = true;
    boolean replayBest = true;
    double[] vision;

    Timer timer;
    Random random;
    Simulate simulate = new Simulate(PANEL_WIDTH,PANEL_HEIGHT,Cell);

    public Game() {

        random = new Random();
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new ControlWithKeys(this));
        run();
        sim();

    }




    public void run() {
        cords=simulate.initSnake(cords);
        placeApple();
        running = true;
        timer = new Timer(75, this);
        timer.start();

    }
    private void sim() {
        snakePath= new ArrayList<>();
        foodList= new ArrayList<>();
        simulate.snake();
        for(Coordinate[] c:simulate.snakePath)
        snakePath.add(c);
        for(Coordinate c:simulate.foodList)
        foodList.add(c);


    }

    private void subsim() {

        if(step<snakePath.size()) {
            bodyValue=snakePath.get(step).length;
            for (int i = 0; i < snakePath.get(step).length; i++)
                cords[i] = snakePath.get(step)[i];

        }
        step++;

    }

    private void drawBest() {
        int foodInx=0;
        for(int i=0; i<snakePath.size();i++) {
            drawApple(this.getGraphics(), foodList.get(foodInx));
            drawSnake(this.getGraphics(), snakePath.get(i));
        }
    }


    public void placeApple() {
        apple= new Coordinate((random.nextInt((int) (PANEL_WIDTH / (2 * Cell))) * Cell),random.nextInt((int) (PANEL_HEIGHT / Cell)) * Cell);

    }
    public void snakes() {
        move();
        if(collisions.checkCollision(cords,bodyValue)){
           running=false;}
        checkRun();
        checkFood();
}

    private void checkRun() {
        if (!running) {
            timer.stop();
        }
    }
    public void move() {
        cords[bodyValue]= new Coordinate();
        for (int i = bodyValue; i > 0; i--) {
            cords[i].setY(cords[i-1].getY());
            cords[i].setX(cords[i-1].getX());
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
    public void checkFood() {
        if (collisions.foodCollide(cords[0],apple)) {
            bodyValue++;
            Score++;
            placeApple();
        }
    }

    public void gameOver(Graphics g) {
        drawScoreboard(g);
        drawGameOver(g);
    }
    public void draw(Graphics g) {

        if (running) {
            drawBoard(g);
            drawApple(g,apple);
            drawSnake(g);
            if (replay && seeVision)
                drawVision(g);
            drawScoreboard(g);
        } else {
            gameOver(g);
        }

    }

    private void drawSnake(Graphics g) {
        if (step>1)
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
    private void drawSnake(Graphics g, Coordinate[] sCoords) {
            for (int i = 0; i < sCoords.length; i++) { // drawing Snake
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(sCoords[i].getX() + 500, sCoords[i].getY(), Cell, Cell);
                } else {

                    g.setColor(new Color(40, 160, 0));
                    g.fillRect(sCoords[i].getX() + 500, sCoords[i].getY(), Cell, Cell);
                }
            }
        }
    private void drawBoard(Graphics g) {
        for (int i = 0; i < PANEL_HEIGHT / Cell; i++) { // drawing of snake board lines
            g.drawLine(500 + i * Cell, 0, 500 + i * Cell, PANEL_HEIGHT);
            g.drawLine(500, i * Cell, PANEL_WIDTH, i * Cell);
        }
    }

    private void drawApple(Graphics g,Coordinate f) {
        g.setColor(Color.red);// drawing apple
        g.fillOval(f.getX() + 500, f.getY(), Cell, Cell);
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
    private void drawVisionLine(int xVector, int yVector, Coordinate head, Graphics g) {
            int cSize = 6;
            int cPos = (Cell - cSize)/2;
            int i=1;
            Coordinate temp = new Coordinate(head.getX(), head.getY());
            do{
                temp.setX(head.getX() + xVector * i + cPos);
                temp.setY(head.getY() + yVector * i  + cPos);

                if(collisions.wallCollide(temp))
                    return;
                g.drawOval(temp.getX() + 500, temp.getY(), cSize, cSize);
                i++;

            }while(i<PANEL_HEIGHT/Cell);
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

        if (running) {
            if(!simulation){
                step=2;
                snakes();}
            else
                subsim();
        }
        repaint();
    }
}
