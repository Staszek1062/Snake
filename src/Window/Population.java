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
    NeuralNetwork bestGenBrain;
    List<Coordinate[]> bestGenPath;
    List<Coordinate> bestFoodList;
    NeuralNetwork bestSnakeBrain;
    List<Coordinate[]> bestSnakePath;
    SimSnake simulate;

    public Population(int population,int panelWidth, int panelHeight, int cell){
         bestSnakeScore=-1;
         this.population = population;
         this.panelWidth = panelWidth;
         this.panelHeight = panelHeight;
         this.cell = cell;
    }
    public void start(){
        int curPop=population;
        bestSnakeScore=0;

        System.out.println("Generacja: "+generation);
        while (curPop>0) {
            simulate = new SimSnake(panelWidth, panelHeight, cell);
            simulate.snake();
            if(generation>1)
                simulate.getBrain().learnFromParent(bestGenBrain,generation);

            if(bestSnakeScore<simulate.getSnakeScore()){
                bestSnakePath=simulate.getSnakePath();
                bestFoodList=simulate.getFoodList();
                bestSnakeBrain=simulate.getBrain();
                bestSnakeScore=simulate.getSnakeScore();
                System.out.println(simulate.getSnakeScore());
            }
            curPop--;
        }
        generation++;
        bestGenBrain = bestSnakeBrain;
        bestGenPath = bestSnakePath;

    }
    public List<Coordinate[]> getBestSnakePath() {
        return bestSnakePath;
    }

    public List<Coordinate> getBestFoodList() {
        return bestFoodList;
    }
}
