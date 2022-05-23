package Window;

import java.io.Serializable;

public class Collisions implements Serializable {
    private final int panelWidth;
    private final int panelHeight;

    public Collisions(int panelWidth,int panelHeight) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
    }

    public boolean checkCollision(Coordinate[] body,int size) {
        return (wallCollide(body[0]) || bodyCollide(body,size));
    }

    boolean bodyCollide(Coordinate[] body,int size) {
        for (int i = size-1; i > 1; i--) {

            if ((body[0].getX() == body[i].getX()) && (body[0].getY() == body[i].getY())){

                return true;
            }

        }
        return false;
    }
    boolean bodyCollide(Coordinate[] body,Coordinate coord,int size) {
        for (int i = size-1; i > 1; i--) {
            if ((coord.getX() == body[i].getX()) && (coord.getY() == body[i].getY())){

                return true;
            }
        }
        return false;
    }

    boolean wallCollide(Coordinate coord) {

        if (coord.getX() >= panelHeight-500 || coord.getX() < 0 || coord.getY() >= panelWidth || coord.getY() < 0) {

            return true;
        }
        return false;
    }

    boolean foodCollide(Coordinate coords, Coordinate apple) {

        if (coords.getX() == apple.getX()  && coords.getY() == apple.getY()) {
            return true;
        }
        return false;
    }
}