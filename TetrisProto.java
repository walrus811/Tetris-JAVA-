import javax.swing.*;
import java.awt.*;

/**
 * Created by walrus on 17. 1. 9.
 */
public class TetrisProto extends JFrame{
    private final int WIDTH=800;
    private final int HEIGHT=880;
    private MapPanel map=new MapPanel();

    public TetrisProto(){
        super();
        drawUI();

    }
    public static void main(String[] args){
        TetrisProto t=new TetrisProto();
    }
    private void drawUI(){
        JFrame frame=new JFrame();
        setBackground(Color.black);
        map.setBackground(Color.black);
        setLayout(null);
        getContentPane().add(map);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setTitle("Tetris Prototype");
        setVisible(true);
    }
}
