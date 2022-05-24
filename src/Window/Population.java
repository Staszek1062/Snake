package Window;

import AIresorces.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

public class Population {


    int panelWidth;
    int panelHeight;
    int cell;
    int population;
    int generation=1;

    int bestSnakeScore;
    NeuralNetwork bestSnakeBrain;
    List<Coordinate[]> bestSnakePath;
    List<Coordinate> bestFoodList;

    SimSnake simulate;

    public Population(int population,int panelWidth, int panelHeight, int cell){
         bestSnakeScore=-1;
         this.population = population;
         this.panelWidth = panelWidth;
         this.panelHeight = panelHeight;
         this.cell = cell;
    }
    public void start(){
        while (population>0) {
            simulate = new SimSnake(panelWidth, panelHeight, cell);
            simulate.snake();
            if(generation>1)
                simulate.getBrain().learnFromParent(bestSnakeBrain,generation);

            if(bestSnakeScore<simulate.getSnakeScore()){
                bestSnakePath=simulate.getSnakePath();
                bestFoodList=simulate.getFoodList();
                bestSnakeBrain=simulate.getBrain();
                bestSnakeScore=simulate.getSnakeScore();
                System.out.println(simulate.getSnakeScore());
            }
            population--;
        }
        generation++;
    }



    public List<Coordinate[]> getBestSnakePath() {
        return bestSnakePath;
    }

    public List<Coordinate> getBestFoodList() {
        return bestFoodList;
    }
}
