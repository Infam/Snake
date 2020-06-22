import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Arrays;


public class World extends JFrame{

    public World() {
        add(new Board());
        pack();
        setTitle("Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                World world = new World();
                world.setVisible(true);
            }
        });
    }
}
class Board extends JPanel implements ActionListener{

    private final int DELAY = 100;
    private final int DIMENSIONX = 1200;
    private final int DIMENSIONY = 700;
    private boolean S1WIN, S2WIN, GameOver;
    private Timer timer;

    private Snake snake;
    private final int S1STARTX= 60;
    private final int S1STARTY= 60;

    private Snake snake2;
    private final int S2STARTX = DIMENSIONX - 60;
    private final int S2STARTY = DIMENSIONY - 60;

    private int actioncountersnake = 1;
    private int actioncountersnake2 = 1;
    // stop 2 direction changes in one turn
    private Food food;
    private boolean UP,DOWN,LEFT,RIGHT;
    private boolean UP2,DOWN2,LEFT2,RIGHT2;
    // create x and y
    public Board(){
        food = new Food();
        snake = new Snake(S1STARTX, S1STARTY, 'r', Color.GREEN);
        snake2 = new Snake(S2STARTX, S2STARTY, 'l', Color.BLUE);
        addKeyListener(new S1Adapter());
        addKeyListener(new S2Adapter());
        setBackground(Color.BLACK);
        setFocusable(true);
        LEFT = false;
        LEFT2 = true;
        UP = false;
        UP2 = false;
        DOWN = false;
        DOWN2 = false;
        RIGHT = true;
        RIGHT2 = false;
        S1WIN = false;
        S2WIN = false;
        start();
    }
    public void start(){
        for (int z = 0; z < snake.length; z++) {
            snake.x[z] = S1STARTX - z * 20;
            snake.y[z] = S1STARTY;
        }
        for (int z = 0; z < snake2.length; z++) {
            snake2.x[z] = S2STARTX + z * 20;
            snake2.y[z] = S2STARTY;
        }
        placeFood(food,snake);
        placeFood(food,snake2);
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void actionPerformed(ActionEvent e) {
        snake.direction = this.getDirection();
        snake2.direction = this.getDirection2();
        snake.grow(food);
        snake2.grow(food);
        //checks if food has been eaten
        snake.move();
        snake2.move();
        placeFood(food,snake);
        placeFood(food,snake2);
        repaint();
        actioncountersnake = 1;
        actioncountersnake2 = 1;
        // stop 2 direction changes in one turn
        if(GameOver){
            timer.stop();
            return;
        }
        S2WIN = collisionDetect(snake,snake2);
        S1WIN = collisionDetect(snake2, snake);
        if(S2WIN){
            timer.stop();
            GameOver = true;
            return;
        }
        if(S1WIN){
            timer.stop();
            GameOver = true;
            return;
        }
    }
    //collision detect is the method that checks for snakes of self collision,
    //collision with other snakes, and collision with the wall.
    public boolean collisionDetect(Snake s, Snake s2)
    {
        //self collision
        for (int n = 1; n < s.length; n++)
        {
            if((s.y[0] == s.y[n])&&(s.x[0] == s.x[n]))
            {
                return true;
            }
        }
        //other snake collision
        for (int j = 1; j < s2.length; j++)
        {
            if((s.y[0] == s2.y[j])&&(s.x[0] == s2.x[j]))
            {
                return true;
            }
        }
        //wall collision
        if((s.x[0] < 0)||(s.y[0] < 0))
            return true;
        if((s.x[0] >  DIMENSIONX - 20)||(s.y[0] >  DIMENSIONY - 20))
            return true;
        return false;
    }
    // checks to see if food and snake coordinates collide
    public boolean onSnake(Food f, Snake s){
        for (int n = 1; n < s.length; n++)
        {
            if((f.x == s.x[n])&&(f.y == s.y[n]))
            {
                return true;
            }
        }
        return false;
    }
    //places the food
    public void placeFood(Food f,Snake s){
        if((f.isEaten == true)||(s.ate = true)){
            while(onSnake(f,s)){
                f.x = (int) Math.floor(Math.random() * f.getRangeX())*20;
                f.y = (int) Math.floor(Math.random() * f.getRangeY())*20;
            }
            f.isEaten = false;
        }
    }
    //prints the ending panel
    //also detects which snake won, or if the game was a tie, and prints the
    //results after clearing the screen.
    public void endGame(Graphics g){
        String msg = "GAME OVER";
        String S1win = "PLAYER 1 WINS";
        String S2win = "PLAYER 2 WINS";
        String scores = "SCORES";
        String Tie = "TIE";
        String CombS = "P1: " + snake.score + "    " + "P2: " + snake2.score;
        String s = CombS;
        Font title = new Font("Helvetica", Font.BOLD, 40);
        Font header = new Font("Helvetica", Font.BOLD, 20);
        Font scoresf = new Font("Helvetica", Font.BOLD, 14);

        g.setColor(Color.RED);
        FontMetrics metr = getFontMetrics(title);
        g.setFont(title);
        g.drawString(msg, (DIMENSIONX - metr.stringWidth(msg)) / 2, (DIMENSIONY / 2) - 30);
        metr = getFontMetrics(header);
        g.setFont(header);
        if((S1WIN == true)&&(S2WIN == true)){
            g.setColor(Color.MAGENTA);
            g.drawString(Tie, (DIMENSIONX - metr.stringWidth(Tie)) / 2, (DIMENSIONY / 2) );
        }
        else{
            if(S1WIN == true){
                g.setColor(Color.GREEN);
                g.drawString(S1win, (DIMENSIONX - metr.stringWidth(S1win)) / 2, (DIMENSIONY / 2) );
            }
            if(S2WIN == true){
                g.setColor(Color.BLUE);
                g.drawString(S2win, (DIMENSIONX - metr.stringWidth(S2win)) / 2, (DIMENSIONY / 2) );
            }
        }
        g.setColor(Color.YELLOW);
        g.drawString(scores, (DIMENSIONX - metr.stringWidth(scores)) / 2, (DIMENSIONY / 2) + 30 );
        metr = getFontMetrics(scoresf);
        g.setColor(Color.WHITE);
        g.drawString(s, (DIMENSIONX - metr.stringWidth(s) - 40) / 2, (DIMENSIONY / 2) + 60 );
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(GameOver){
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, DIMENSIONX, DIMENSIONY);
            endGame(g);
            return;
        }
        food.drawFood(g);
        snake.drawSnake(g);
        snake2.drawSnake(g);
    }

    public char getDirection(){
        if(UP){
            return 'u';
        }
        if(DOWN){
            return 'd';
        }
        if(RIGHT){
            return 'r';
        }
        if(LEFT){
            return 'l';
        }
        else{
            return '!';
        }
    }
    public char getDirection2(){
        if(UP2){
            return 'u';
        }
        if(DOWN2){
            return 'd';
        }
        if(RIGHT2){
            return 'r';
        }
        if(LEFT2){
            return 'l';
        }
        else{
            return '!';
        }
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DIMENSIONX, DIMENSIONY);
    }

    class S1Adapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(actioncountersnake == 1){
                int key = e.getKeyCode();

                if ((key == KeyEvent.VK_LEFT) && (!RIGHT)) {
                    LEFT = true;
                    UP = false;
                    DOWN = false;
                }

                if ((key == KeyEvent.VK_RIGHT) && (!LEFT)) {
                    RIGHT = true;
                    UP = false;
                    DOWN = false;
                }

                if ((key == KeyEvent.VK_UP) && (!DOWN)) {
                    UP = true;
                    RIGHT = false;
                    LEFT = false;
                }

                if ((key == KeyEvent.VK_DOWN) && (!UP)) {
                    DOWN = true;
                    RIGHT = false;
                    LEFT = false;
                }
                if (key == KeyEvent.VK_G){
                    GameOver = true;
                }
                actioncountersnake = 0;
            }
        }
    }
    class S2Adapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            if(actioncountersnake2 == 1){
                int key = e.getKeyCode();

                if ((key == KeyEvent.VK_A) && (!RIGHT2)) {
                    LEFT2 = true;
                    UP2 = false;
                    DOWN2 = false;
                }

                if ((key == KeyEvent.VK_D) && (!LEFT2)) {
                    RIGHT2 = true;
                    UP2 = false;
                    DOWN2 = false;
                }

                if ((key == KeyEvent.VK_W) && (!DOWN2)) {
                    UP2 = true;
                    RIGHT2 = false;
                    LEFT2 = false;
                }

                if ((key == KeyEvent.VK_S) && (!UP2)) {
                    DOWN2 = true;
                    RIGHT2 = false;
                    LEFT2 = false;
                }
                if (key == KeyEvent.VK_G){
                    GameOver = true;
                }
                actioncountersnake2 = 0;
            }
        }
    }
}
