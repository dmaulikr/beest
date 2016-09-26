import java.awt.*;


public class Box {

    private int x, y;
    public int dir;

    public Box(int x, int y) {          //standard box constructor, fills box object with standard values
        this.x = (x * 40) + 4;
        this.y = (y * 40) + 4;
    }

    public int getX() {         //standard X and Y getter values, used for math/drawing
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getRect() {                //standard rectangle getter, used for most collisions
        return new Rectangle(x, y, 30, 30);
    }

    public Rectangle getRect(int a) {       //special rectangle getter, used to deterime the outer bounds for a rectangle's area
        return new Rectangle(x, y, 75, 75);
    }

    public boolean shove(int dir) {         //shove is a reaction method the box uses when the player runs into it. Will shove the box forward, unless there is a wall in the way.
        this.dir = dir;
        switch (dir) {
            case 1:
                if (y <= 20) {
                    return true;
                } else {
                    y -= 40;
                    return false;
                }
            case 2:
                if (y >= 450) {
                    return true;
                } else {
                    y += 40;
                    return false;
                }
            case 3:
                if (x <= 20) {
                    return true;
                } else {
                    x -= 40;
                    return false;
                }
            case 4:
                if (x >= 707) {
                    return true;
                } else {
                    x += 40;
                    return false;
                }
        }
        return false;
    }
    public void pushBack(int dir){      //pushback in case the box slides into another box when pushed
        switch (dir){
            case 1:
                y -= 40;
                break;
            case 2:
                y += 40;
                break;
            case 3:
                x -= 40;
                break;
            case 4:
                x += 40;
                break;
        }
    }
    public void draw(Graphics g) {          //drawing of box deferred to the box class
        g.setColor(Color.black);
        g.fillRect(x,y, 32, 32);
    }
}
