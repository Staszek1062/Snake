package Game;

import Snake.Snake;

public class Swarm {

   /* Snake[] swarm;
    Snake bestSnake;

    int bestScore = 0;
    int gen = 0;
    int sameBest = 0;

    float bestFitness = 0;
    float fitnessSum = 0;

    public Swarm(int size){
        swarm = new Snake[size];
        for(int i = 0; i < swarm.length; i++) {
            swarm[i] = new Snake();
        }
        bestSnake = swarm[0].copy();
        bestSnake.replay = true;
    }

    boolean populationDead(){
        for(Snake snake:swarm){
            if(snake.dead)
                return false;
        }
        if(!bestSnake.dead)
            return false;
        return true;
    }
    void update(){
        if(!bestSnake.dead){
            bestSnake.look();
            bestSnake.think();
            bestSnake.move();
        }
        for(Snake snake:swarm) {
            if(!snake.dead) {
                snake.look();
                snake.think();
                snake.move();
            }
        }

    }
    void show(){
        if(replayBest){
            bestSnake.show();
  //        bestSnake  show best snake decison
        }
        else
            for (Snake snake:swarm)
                snake.show();
    }
    Snake selectParent() {
        float rand = random(fitnessSum);
        float summation = 0;
        for(Snake snake:swarm) {
            summation += snake.fitness;
            if(summation > rand) {
                return snake;
            }
        }
        return swarm[0];
    }*/

}
