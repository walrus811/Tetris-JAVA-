/**
 * Created by walrus on 17. 1. 9.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.TimerTask;
import java.util.Timer;
/*
* 할 일
* 끝처리 추가
* 다음블럭 표시
* UI 꾸미기
* 코드 정리
* jar 배포파일로 작성
* */
public class MapPanel extends JPanel{
    enum Info{WALL, BRICK, NONE};
    class Block{
        private Info info;
        private Color color;

        public Info getInfo() {
            return info;
        }

        public Color getColor() {
            return color;
        }

        public void setInfo(Info info) {
            this.info = info;
        }

        public void setColor(Color color) {
            this.color = color;
        }
    }
    private int originX;
    private int originY;
    private final int WIDTH=800;
    private final int HEIGHT=880;
    private final int MAP_X=12;
    private final int MAP_Y=22;
    private final int NEXT_X=15;
    private final int NEXT_Y=17;
    private final int BRICK_SIZE=40;
    private final Color wall=new Color(103,0,0);
    private final Color none=new Color(0,0,0);

    private Block[][] data;
    private Blick blick;
    private Blick nextBlick;
    private Timer timer =new Timer();
    private boolean onGame;
    private int speed;
    private int count;

    public void reinitialize(){
        onGame=true;
        count=0;
        originX=0;
        originY=0;
        data=new Block[MAP_Y][MAP_X];
        speed=0;

        for(int y=0; y<MAP_Y; y++){
            for(int x=0; x<MAP_X; x++){
                data[y][x]=new Block();
                if(y==0 || y==MAP_Y-1 || x==0 || x==MAP_X-1){
                    data[y][x].setInfo(Info.WALL);
                    data[y][x].setColor(wall);
                }
                else{
                    data[y][x].setInfo(Info.NONE);
                    data[y][x].setColor(none);
                }
            }
        }
        if(nextBlick==null)
            blick=new Blick();
        else
            blick=nextBlick;
        nextBlick=new Blick(NEXT_X,NEXT_Y);
        blick.initBlickPos();
    }
    public MapPanel(){
        reinitialize();
        setBackground(Color.black);
        setBounds(originX,originY, WIDTH, HEIGHT);

        timer.schedule(
                new TimerTask(){
                    @Override
                    public void run() {
                        if(isFailed()) reinitialize();
                        repaint();
                        if(!onGame) return;
                        update();
                    }},
                100,100);

        setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keycode = e.getKeyCode();
                if(e.getKeyCode()==KeyEvent.VK_R ) reinitialize();
                if(e.getKeyCode()==KeyEvent.VK_P){
                    if(onGame) onGame = false;
                    else onGame=true;
                }

                if(!onGame) return;

                if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                    if(isAround(blick.getX()+1,blick.getY(),blick.getForm())==Info.NONE) {
                        blick.move(false);
                    }
                    else{ ; }
                }
                else if(e.getKeyCode()==KeyEvent.VK_LEFT){
                    if(isAround(blick.getX()-1,blick.getY(),blick.getForm())==Info.NONE) {
                        blick.move(true);
                    }
                    else{;}

                }
                else if(e.getKeyCode()==KeyEvent.VK_UP){
                    if(blick.isOMino()) return;
                    blick.turnLeft();
                    if(isAround(blick.getX(),blick.getY(),blick.getForm())!=Info.NONE) {
                        blick.turnRight();
                    }
                    else{;}
                }
                else if(e.getKeyCode()==KeyEvent.VK_DOWN){
                    if(isAround(blick.getX(),blick.getY()+1,blick.getForm())==Info.NONE)
                        blick.moveDown();
                    else{
                        Color c=blick.getColor();
                        for(Delta d :  blick.getForm()) {
                            data[blick.getY() + d.getDy()][blick.getX() + d.getDx()].setInfo(Info.BRICK);
                            data[blick.getY() + d.getDy()][blick.getX() + d.getDx()].setColor(c);

                            if(nextBlick==null)
                                blick=new Blick();
                            else
                                blick=nextBlick;
                            nextBlick=new Blick(NEXT_X,NEXT_Y);
                        }
                    }
                }
                else if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    while(blickDown()){;}
                }
            }

        });

    }//public MapPanel()

    public void update(){
        speed++;
        if(speed==5) {
            if(isAround(blick.getX(),blick.getY()+1,blick.getForm())==Info.NONE)
                blick.moveDown();
            else{
                Color c=blick.getColor();
                for(Delta d :  blick.getForm()) {
                    data[blick.getY() + d.getDy()][blick.getX() + d.getDx()].setInfo(Info.BRICK);
                    data[blick.getY() + d.getDy()][blick.getX() + d.getDx()].setColor(c);

                    if(nextBlick==null)
                        blick=new Blick();
                    else
                        blick=nextBlick;
                    blick.initBlickPos();
                    nextBlick=new Blick(NEXT_X,NEXT_Y);
                }
            }
            speed=0;
        }
        eraseLine();
    }//public void update()

    public boolean blickDown(){
        blick.moveDown();
        int bx=blick.getX();
        int by=blick.getY();
        if(isAround(bx,by+1,blick.getForm())!=Info.NONE){
            Color c=blick.getColor();
            System.out.println(bx+" : "+by);
            for(Delta d :  blick.getForm()){
                data[by+d.getDy()][bx+d.getDx()].setInfo(Info.BRICK);
                data[by+d.getDy()][bx+d.getDx()].setColor(c);

            }
            if(nextBlick==null)
                blick=new Blick();
            else
                blick=nextBlick;
            blick.initBlickPos();
            nextBlick=new Blick(NEXT_X,NEXT_Y);
            return false;
        }
        return true;
    }//public boolean blickDown()

    public void paintComponent(Graphics g){
        paintBackground(g);
        drawBlick(g,blick);
    }//public void paintComponent(Graphics g)

    private void paintBackground(Graphics g){
        for(int y=0; y<HEIGHT /BRICK_SIZE; y++){
            for(int x=0; x<WIDTH/BRICK_SIZE; x++) {
                drawSquare(g, x*BRICK_SIZE,y*BRICK_SIZE, none);
            }
        }
        Graphics2D g2=(Graphics2D)g;
        Color c=none;
        for(int y=0; y<MAP_Y; y++){
            for(int x=0; x<MAP_X; x++){
                Info temp=data[y][x].getInfo();
                c=data[y][x].getColor();
                drawSquare(g, x*BRICK_SIZE,y*BRICK_SIZE, c);
            }
        }//for(int y=0; y<MAP_Y; y++)

        for(int y=MAP_Y-7; y<MAP_Y; y++){
            for(int x=MAP_X; x<MAP_X+8; x++) {
                c=none;
                if(y==MAP_Y-7 || y==MAP_Y-1 || x==MAP_X || x==MAP_X+7) c=wall;
                else c=none;
                drawSquare(g, x*BRICK_SIZE,y*BRICK_SIZE, c);
            }
        }
        drawBlick(g,nextBlick);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(232,217,255));
        g2.setFont(new Font("Sans", Font.BOLD, 40));
        g2.drawString("테트리스", 560,60);

        g2.setColor(new Color(29,219,22));
        g2.setFont(new Font("Sans", Font.BOLD, 25));
        if(onGame) g2.drawString("게임중", 600,150);
        else g2.drawString("일시정지", 590,150);
        g2.setFont(new Font("Sans", Font.BOLD, 15));
        g2.drawString("만든사람 : 일여비", 580,200);
        g2.setColor(new Color(255,0,127));
        g2.setFont(new Font("Sans", Font.BOLD, 35));
        g2.drawString("다음 미노", 570,570);
        g2.setColor(new Color(255,250,127));
        g2.setFont(new Font("Sans", Font.BOLD, 25));
        g2.drawString("지운 줄 수 "+count, 570,370);
        /*
            g2.setColor(new Color(255,20,10));
            g2.setFont(new Font("Sans", Font.BOLD, 35));
            g2.drawString("다시 시작하려면 R버튼!", 500,350);
        */
    }//private void paintBackground(Graphics g)

    private void drawBlick(Graphics g, Blick blick){
        int bx=blick.getX();
        int by=blick.getY();
        for(Delta d :  blick.getForm()){
            drawSquare(g, (bx+d.getDx())*BRICK_SIZE,(by+d.getDy())*BRICK_SIZE, blick.getColor());
        }
    }//private void drawBlick(Graphics g, Blick blick)

    public void eraseLine(){
        boolean check=true;
        for(int y=data.length-2; y>=2; y--){
            check=true;
            for(int x=data[y].length-2; x>=1; x--){
                Info temp=data[y][x].getInfo();
                if(temp!=Info.BRICK){
                    check=false;
                    break;
                }
            }
            if(check){
                for(int y2=y; y2>=2; y2--) {
                    for (int x = data[y2].length-2; x >=1; x--) {
                        data[y2][x].setInfo(data[y2 - 1][x].getInfo());
                        data[y2][x].setColor(data[y2 - 1][x].getColor());
                    }
                }
                count++;
            }//if(check)
        }//for(int y=data.length-2; y>=2; y--)
    }//public void eraseLine()

    private void drawSquare(Graphics g, int x, int y, Color color) {
        g.setColor(color);
        g.fillRect(x + 1, y + 1, BRICK_SIZE - 2, BRICK_SIZE - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y +BRICK_SIZE - 1, x, y);
        g.drawLine(x, y, x + BRICK_SIZE - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + BRICK_SIZE - 1, x + BRICK_SIZE - 1, y + BRICK_SIZE - 1);
        g.drawLine(x + BRICK_SIZE - 1, y + BRICK_SIZE - 1, x + BRICK_SIZE - 1, y + 1);

    }//private void drawSquare(Graphics g, int x, int y, Color color)

    private Info isAround(int x, int y, Delta[] delta){
        Info result=Info.NONE;
        try {
            for (Delta d : delta) {
                Info temp = data[y + d.getDy()][x + d.getDx()].getInfo();
                if (temp == Info.WALL) result = Info.WALL;
                else if (temp == Info.BRICK) result = Info.BRICK;
            }
        }
        catch(ArrayIndexOutOfBoundsException ex){
            return Info.WALL;
        }
        return result;
    }//private Info isAround(int x, int y, Delta[] delta)

    public boolean isFailed(){
        for(Block b  :  data[1]){
            if(b.getInfo()==Info.BRICK) return true;
        }
        return false;
    }// public boolean isFailed()
}
