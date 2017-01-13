/**
 * TetrisProto
 * Created by walrus on 17. 1. 13.
 * on 오후 1:28
 */
public class Delta{
    private int dx;
    private int dy;

    public Delta(){
        this.dx=this.dy=0;
    }
    public Delta(int dx,int dy){
        this.dx=dx;
        this.dy=dy;
    }
    public void setDelta(int dx, int dy){
        this.dx=dx;
        this.dy=dy;
    }
    public int getDx() {
        return dx;
    }
    public int getDy() {
        return dy;
    }

}
