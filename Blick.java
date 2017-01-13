/**
 * Created by walrus on 17. 1. 9.
 */
import java.awt.*;
import java.util.*;

public class Blick {
    private int x;
    private int y;
    private static final int START_X=5;
    private static final int START_Y=1;
    private static final int BLICK_NUM=7;

    private enum Type{I_MINO, O_MINO, Z_MINO, S_MINO,J_MINO, L_MINO, T_MINO}
    private static final Type[] tArr=Type.values();
    private Color colors[] = { new Color(204, 102, 102),
            new Color(102, 204, 102), new Color(102, 102, 204),
            new Color(204, 204, 102), new Color(204, 102, 204),
            new Color(102, 204, 204), new Color(218, 170, 0)
    };
    private static final Random rand=new Random();

    private Type type;
    private Color color;
    Delta[] form;
    public Blick(int x,int y){
        this();
        this.x=x;
        this.y=y;
    }
    public void initBlickPos(){
        x=START_X;
        y=START_Y;
    }
    public Blick(){
        initBlickPos();
        int r=rand.nextInt(BLICK_NUM);
        type=tArr[r];
        color=colors[type.ordinal()];
        form=new Delta[4];
        for(int i=0; i<form.length; i++){
            form[i]=new Delta();
        }
        switch(type) {
            /*
            * o
            * x
            * x
            * x
            * */
            case I_MINO:
                form[0].setDelta(0,0);
                form[1].setDelta(0,1);
                form[2].setDelta(0,2);
                form[3].setDelta(0,3);
                break;
            /*
            * ox
            * xx
            * */
            case O_MINO:
                form[0].setDelta(0,0);
                form[1].setDelta(0,1);
                form[2].setDelta(1,0);
                form[3].setDelta(1,1);
                break;
            /*
            * xo
            *  xx
            * */
            case Z_MINO:
                form[0].setDelta(0,0);
                form[1].setDelta(0,1);
                form[2].setDelta(1,1);
                form[3].setDelta(-1,0);
                break;
            /*
            *  ox
            * xx
            * */
            case S_MINO:
                form[0].setDelta(0,0);
                form[1].setDelta(0,1);
                form[2].setDelta(-1,1);
                form[3].setDelta(1,0);
                break;
            /*
            * o
            * xxx
            * */
            case J_MINO:
                form[0].setDelta(0,1);
                form[1].setDelta(0,0);
                form[2].setDelta(1,1);
                form[3].setDelta(2,1);
                break;
            /*
            *   o
            * xxx
            * */
            case L_MINO:
                form[0].setDelta(0,1);
                form[1].setDelta(0,0);
                form[2].setDelta(-1,1);
                form[3].setDelta(-2,1);
                break;
            /*
            *  o
            * xxx
            * */
            case T_MINO:
                form[0].setDelta(0,1);
                form[1].setDelta(1,1);
                form[2].setDelta(-1,1);
                form[3].setDelta(0,0);
                break;
        }
    }//public Blick()

    public void turnLeft(){
        for(int i=0; i<form.length; i++){
            int tx=form[i].getDx();
            int ty=form[i].getDy();
            form[i].setDelta(-ty,tx);
        }
    }//public void turnLeft()

    public void turnRight(){
        for(int i=0; i<form.length; i++){
            int tx=form[i].getDx();
            int ty=form[i].getDy();
            form[i].setDelta(ty,-tx);
        }
    }//public void turnRight()

    public void moveDown(){
        this.y++;
    }

    public void move(boolean isLeft){
        if(isLeft) this.x--;
        else this.x++;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Delta[] getForm() {
        return form;
    }

    public boolean isOMino(){return type==Type.O_MINO;}
}
