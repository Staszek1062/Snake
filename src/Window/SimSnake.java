package Window;

import AIresorces.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimSnake {
    private int panelWidth;
    int panelHeight;
    int cell;
    private final Collisions collisions;
    public static int TotalCells;
    int timeleft=200;
    int foodIdx=0;
    int simScore=0;
    int direction = 3;
    int bodyValue = 3;
    double[] vision;
    boolean snakeDead = false;

    Random random;
    Coordinate apple;
    List<Double> decisions;
    Coordinate[] coords;
    List<Coordinate> foodList=new ArrayList<>();
    List<Coordinate[]> snakePath= new ArrayList<>();



    NeuralNetwork brain = new NeuralNetwork(24,20,4,0.04);
    public NeuralNetwork getBrain() {
        return brain;
    }
    public List<Coordinate> getFoodList() {
        return foodList;
    }

    public List<Coordinate[]> getSnakePath() {
        return snakePath;
    }

    public int getSnakeScore() {
        return simScore;
    }

    public SimSnake(int panelWidth, int panelHeight, int cell) {

        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.cell = cell;
        collisions = new Collisions(panelHeight,panelWidth);
        TotalCells = (panelHeight * panelWidth) / (cell * cell * 2);
         coords = new Coordinate[TotalCells];
    }


    public void snake() {
        initSnake(coords);

        int i =0;
        while(i<=timeleft&&!snakeDead){

            Coordinate[] currentCoords= getCoordinates(coords,bodyValue);
            if (collisions.wallCollide(currentCoords[0]) || collisions.bodyCollide(currentCoords,currentCoords.length)){

            snakeDead =true;
            }

            if(collisions.foodCollide(currentCoords[0],foodList.get(foodIdx))) {
                placeApple();
                foodIdx++;
            }
            snakeVision();
            snakeThink();
            move();
            Coordinate[] temp = new Coordinate[bodyValue];
            for(int j = 0; j < bodyValue; j++) {
                temp[j]=new Coordinate(coords[j].getX(),coords[j].getY());
            }
            snakePath.add(temp);
            i++;
            timeleft=200+foodIdx*10;
        }
        simScore=i+foodIdx*50;
    }

    private void snakeThink() {
        double max;
        decisions=new ArrayList<>(4);
        decisions.addAll(brain.predict(vision));
        max= (decisions.get(0)>decisions.get(1))?decisions.get(0):decisions.get(1);
        max= (max>decisions.get(2))?max:decisions.get(2);
        max= (max>decisions.get(3))?max:decisions.get(3);
        if(!((decisions.indexOf(max)+1)%2==direction%2))
        direction = (decisions.indexOf(max)+1);
    }
    private void snakeVision() {
        vision = new double[24];
        double[] temp = checkVisionLine( cell, cell, coords[0]);
        vision[0] = temp[0];
        vision[1] = temp[1];
        vision[2] = temp[2];
        temp = checkVisionLine( cell, 0, coords[0]);
        vision[3] = temp[0];
        vision[4] = temp[1];
        vision[5] = temp[2];
        temp = checkVisionLine( cell, -cell, coords[0]);
        vision[6] = temp[0];
        vision[7] = temp[1];
        vision[8] = temp[2];
        temp = checkVisionLine( 0, -cell, coords[0]);
        vision[9] = temp[0];
        vision[10] = temp[1];
        vision[11] = temp[2];
        temp = checkVisionLine( -cell, -cell, coords[0]);
        vision[12] = temp[0];
        vision[13] = temp[1];
        vision[14] = temp[2];
        temp = checkVisionLine( -cell, 0, coords[0]);
        vision[15] = temp[0];
        vision[16] = temp[1];
        vision[17] = temp[2];
        temp = checkVisionLine( -cell, cell, coords[0]);
        vision[18] = temp[0];
        vision[19] = temp[1];
        vision[20] = temp[2];
        temp = checkVisionLine( 0, cell, coords[0]);
        vision[21] = temp[0];
        vision[22] = temp[1];
        vision[23] = temp[2];

    }
    public void placeApple() {
        apple= new Coordinate((random.nextInt((int) (panelWidth / (2 * cell))) * cell),random.nextInt((int) (panelHeight / cell)) * cell);
        foodList.add(apple);
    }
    private double[] checkVisionLine(int xVector, int yVector, Coordinate head) {
        double[] results = new double[3];
        int i = 1;
        Coordinate temp = new Coordinate(head.getX(), head.getY());
        do{
            temp.setX(head.getX() + xVector * i );
            temp.setY(head.getY() + yVector * i );
            if(collisions.foodCollide(temp,apple))
                results[0]=1;
            if(collisions.bodyCollide(coords,temp,bodyValue))
                results[1]=1;
            i++;
        }while(!collisions.wallCollide(temp));
        results[2]=1/i;
        return results;
    }

    public void move() {
        //Coordinate[] temp = getCoordinates(coords,bodyValue);
        switch (direction) {
            case 1: // left
                coords[0].setX(coords[0].getX() - cell
                );
                break;
            case 2: // up
                coords[0].setY(coords[0].getY() - cell
                );
                break;
            case 3: // right
                coords[0].setX(coords[0].getX() + cell
                );
                break;
            case 4: // down
                coords[0].setY(coords[0].getY() + cell
                );
                break;
        }

    }

    public Coordinate[] getCoordinates(Coordinate[] coordinates,int lenght) {
        for (int i = lenght-1; i > 0; i--) {
            coordinates[i].setX(coordinates[i - 1].getX());
            coordinates[i].setY(coordinates[i - 1].getY());
        }
        Coordinate[] temp= new Coordinate[lenght];
        for (int i = lenght-1; i >= 0; i--) {
            temp[i]= coords[i];
        }
        return temp;
    }

    public Coordinate[]  initSnake(Coordinate[] coordinates) {
        random = new Random();
        placeApple();
        for (int i = 0; i < TotalCells; i++)
            coordinates[i]= new Coordinate();
        coordinates[0].setX(cell*10);
        coordinates[0].setY(cell*10);
        return  coordinates;
    }
}
