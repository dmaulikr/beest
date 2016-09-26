import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;


public class Panel extends JPanel implements KeyListener{
    //all global variables declared
    private int p_height, p_width, level, score, boxesNum, enemyNum, direction, loopCount;
    private Player ply;
    private Timer timer;
    private boolean ttb, toggle;
    private java.util.Timer timerUtil;
    private ArrayList<Box> boxes;
    private ArrayList<Beest> beestjes;

    public Panel(){     //protected way to create main game panel
        initPanel();
    }
    private void initPanel(){
        //global variables are initialized
        p_height = 550;
        p_width = 767;
        toggle = true;
        ply = new Player(404, 284);
        level = 0;
        ttb = false;
        score = 0;
        enemyNum = 5;
        boxesNum = 15;
        loopCount = 0;
        boxes = new ArrayList<>();
        beestjes = new ArrayList<>();
        timerUtil = new java.util.Timer();
        setSize(p_width, p_height);

        //adding any neccesary listeners, and putting the finishing touches on the JPanel
        addKeyListener(this);
        setVisible(true);
        setFocusable(true);

        //Here, the AWT timer is used in order to update the visual pieces of the project
        timer = new Timer(14, e -> {
            repaint();
        });
        timer.start();

        //The java UtilTime is used here, as it is more efficiently designed for updating the backend of visual projects, such as ArrayLists.
        timerUtil.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkCollisions();
                boxCollisions();
                checkLevelWin();
                loopCount++;
                if(loopCount >=200){
                    loopCount = 0;
                    for(Beest b: beestjes){
                        b.move(ply.getX(), ply.getY(), boxes);
                    }
                }
            }
        }, 7, 14);
    }

    //the first level being activated. There must be a boolean-based toggle here to ensure the level is not rerendered each time the player presses a key
    private void levelUp(){
        if(level == 0 && toggle){
            levelGen();
            toggle = false;
        }
    }

    //overall paint method
    public void paint(Graphics g){

        //background drawn before everything else
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, 10000, 10000);


            if (!ttb) {           //opening screen, with instructions!
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", 23, 35));
                g.drawString("Welcome to Beest!", (p_width / 2 - 150 ), p_height / 3 - 100);
                g.setColor(Color.blue);
                g.drawString("Instructions: WASD to move", (p_width / 2 - 200), p_height  - 300);
                g.setColor(Color.black);
                g.drawString("Objective: Push boxes to crush the nasty beests!", (p_width -750), p_height - 200);
                g.setColor(Color.magenta);
                g.drawString("Press any button to begin!", (p_width - 600), p_height - 100);


            } else {              //drawing of boxes and beests is kicked to their respective classes
                g.setColor(Color.darkGray);

                g.fillOval(ply.getX(), ply.getY(), 32, 32); //player is drawn manually, as the sprite is simple

                for (Box b : boxes) {
                    b.draw(g);
                }
                g.setColor(Color.red);
                for (Beest b : beestjes) {
                    b.draw(g);
                }

                //Score and current round are kept track of and painted on the corner of the screen constantly
                g.setColor(Color.white);
                g.drawString("Score: " + score, 25, 50);
                g.drawString("Round: " + level, 25, 25);
            }
    }

    //levelgenerating method, each level is generated randomly.
    public void levelGen(){
        level++;

        //boxes are generated (care is taken to see they dont overlap with the player's spawn position)
        for(int b = 0; b <= boxesNum; b++){
            Random rand = new Random();
            int xTT = rand.nextInt(19);
            int yTT = rand.nextInt(13);
            if (xTT == ply.getX() && yTT == ply.getY()){        //check for overlap
            }else {
                boxes.add(new Box(xTT, yTT));
            }
        }

        //enemies are generated (care is taken to see they dont overlap with the player's spawn position)
        for(int b = 0; b <= enemyNum; b++){
            Random rand = new Random();
            int xTT = rand.nextInt(19);
            int yTT = rand.nextInt(13);
            if (xTT == ply.getX() && yTT == ply.getY()){        //check for overlap
            }else {
                beestjes.add(new Beest(xTT, yTT));
            }
        }

        //each time a level passes, the difficulty is ramped up by reducing the # of boxes you have and increasing the number of enemies
        boxesNum--;
        enemyNum++;
    }

        //here, we check if the boxes collided with each other. If they do, the boxes are shoved away from each other in a neat line.
    public void boxCollisions(){
        for(int i = 0; i < boxes.size() ; i++) {
            Rectangle b1 = new Rectangle(boxes.get(i).getRect());
            for (int j = 0; j < boxes.size(); j++) {
                Rectangle b2 = new Rectangle(boxes.get(j).getRect());
                if(b2.intersects(b1) && i!=j){
                    boxes.get(j).pushBack(direction);
                }
            }
        }
    }

    //checks if level has been cleared by killing all beestjes
    public void checkLevelWin(){
        if(beestjes.isEmpty()){
            //remaining objects are removed from the game board!
            for(int i = boxes.size() - 1; i >= 0; i--){
                boxes.remove(i);
            }

            //clean slate can be used to generate a fresh level
            levelGen();
        }
    }

    //all other collision checking is handled here!
    public void checkCollisions() {
        Rectangle pR = ply.getRect();
        //this is section checks if the player pushed a box into a wall, and if the player should be pushed back as a result
        for (int i = 0; i < boxes.size(); i++) {
            Rectangle bR = boxes.get(i).getRect();
            if (pR.intersects(bR)) {
                if (boxes.get(i).shove(direction)) {
                    ply.pushBack(direction);
                    break;
                }
            }

                //this is section checks if the player pushed a box into a beest, and kills the beest if it was done!
                for (int xx = 0; xx < beestjes.size(); xx++) {
                    Rectangle beeR = beestjes.get(xx).getRect();
                    if (bR.intersects(beeR)) {
                        beestjes.remove(xx);
                        xx--;
                        score++;
                    }

                    //this section handles the unfortunate event of player death :(
                    else if (beeR.intersects(pR)) {
                        //all difficulty/toggle values are reset
                        enemyNum = 5;
                        boxesNum = 15;
                        ttb = false;

                        //player is told how they did!
                        JOptionPane.showMessageDialog(this, "You Died!");
                        for (int ii = beestjes.size() - 1; ii >= 0; ii--) {
                            beestjes.remove(ii);
                        }
                        for (int iii = boxes.size() - 1; iii >= 0; iii--) {
                            boxes.remove(iii);
                        }
                        JOptionPane.showMessageDialog(this, "Final Score: " + score + "/Rounds Completed: " + level);

                        //game restarts!!!
                        level = 0;
                        score = 0;
                        levelGen();

                    }
                }
            }


    }

    //KeyListener methods
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {
        int kp = e.getKeyCode();

        //this section handles the creation of the first level, the "press any key to continue" prompt shown at the opening menu
        if(!ttb){
            levelUp();
        }
        ttb = true;


        //these methods handle player movement
        if(kp == KeyEvent.VK_W && ply.getY() >= 20){
            direction = 1;
            ply.moveUp();
        }
        if(kp == KeyEvent.VK_S && ply.getY() <= p_height - 60){
            direction = 2;
            ply.moveDown();
        }
        if(kp == KeyEvent.VK_A && ply.getX() >= 20){
            direction = 3;
            ply.moveLeft();

        }
        if(kp == KeyEvent.VK_D && ply.getX() <= p_width - 60){
            direction = 4;
            ply.moveRight();
        }
    }
}
