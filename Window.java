import javax.swing.*;


public class Window extends JFrame {
    public Window(){
        initWindow();
    }
    private void initWindow(){          //creates the outlined JFrame
        setSize(767, 550);
        setResizable(false);
        setTitle("BEEST 2.0");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main (String [] args){       //creates JFrame, adds JPanel which is used for drawing
        Window app = new Window();
        Panel pane = new Panel();
        app.add(pane);
        app.setVisible(true);
    }
}
