import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public class Beest {            //fun fact, Beest is Dutch for beast!

    private int x, y, dir;

    public Beest(int x, int y) {        //constructor, fills a beest w/ it's x and y values
        this.x = (x * 40) + 4;
        this.y = (y * 40) + 4;
        dir = 1;
    }

    public int getX() {
        return x;
    }   //standard x and y getter methods so they can be used for math

    public int getY() {
        return y;
    }

    public Rectangle getRect() {        //standard rectangle getter method, used for collisions
        return new Rectangle(x, y, 30, 30);
    }

    public void draw(Graphics g) {      //drawing of sprite handled within the Beest class
        g.setColor(Color.red);
        g.fillOval(x, y, 30, 30);
        g.setColor(Color.black);
        g.fillOval(x + 20, y + 5, 5, 5);
        g.fillOval(x + 10, y + 5, 5, 5);
        g.fillRoundRect(x + 7, y + 15, 20, 5, 360, 360);
    }

    public void move(int plyX, int plyY, ArrayList<Box> boxes) {        //complex AI based movement for beests
        Random rand = new Random();


        for (Box box : boxes) {                     //this section determines if a beest is near a box, and if so, which direction a beest must use to dodge it.
            Rectangle boxBounds = box.getRect(2);
            if (new Rectangle(x, y, 50, 50).intersects(boxBounds)) {
                switch (dir) {
                    case 1:
                        y += 40;
                        break;
                    case 2:                     //NOTE: 1 is UP, 2 is DOWN, 3 is LEFT, 4 is RIGHT.
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


                boolean tt = rand.nextBoolean();
                if (plyX == x) {            //if the beest is on the same x-axis as the player, run towards the player
                    if (plyY > y) {
                        y += 40;
                        dir = 2;
                        break;
                    } else {
                        y -= 40;
                        dir = 1;
                        break;
                    }
                } else if (plyY <= y + 10 && plyY >= y - 10) {      //if the beest is on the same y-axis as the player, run towards the player
                    if (plyX > x) {
                        x += 40;
                        dir = 4;
                        break;
                    } else {
                        x -= 40;
                        dir = 3;
                        break;
                    }
                }


                else {                                          //psuedo-random movement, beest decides randomly to move vertically or horizontally, but will always move in the direction of the player.
                    if (tt) {
                        if (plyX > x) {
                            x += 40;
                            dir = 4;
                            break;
                        } else {
                            x -= 40;
                            dir = 3;
                            break;
                        }
                    } else {
                        if (plyY > y) {
                            y += 40;
                            dir = 2;
                            break;
                        } else {
                            y -= 40;
                            dir = 1;
                            break;
                        }
                    }
                }
            }
        }
    }

