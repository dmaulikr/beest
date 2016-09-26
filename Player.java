import java.awt.*;


public class Player {
    private int x, y;

    public Player(int x, int y){        //standard constructor, loads up x and y values for the player object
        this.x = x;
        this.y = y;
    }
    //standard getters, grab X, Y, and rectangle (for collisions)
    public Rectangle getRect(){return new Rectangle(x, y, 30, 30);}
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    //movement methods for player to control with arrow keys
    public void moveUp() {
        y -= 40;
    }
    public void moveDown() {
        y += 40;
    }
    public void moveLeft() {
        x -= 40;
    }
    public void moveRight() {
        x += 40;
    }

    //methods used when the player runs into an obstacle they need to bounce off of!
    public void pushBack(int dir){
        switch (dir){
            case 1:
                y += 40;
                break;
            case 2:
                y -= 40;
                break;
            case 3:
                x += 40;
                break;
            case 4:
                x -= 40;
                break;
        }
    }
}
