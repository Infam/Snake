import java.awt.*;
import java.util.Arrays;
import java.util.ArrayList;

public class Snake {
   public int score;
   public char direction;
   public final int side = 20;
   public final int border = 1;
   public boolean ate;
   public int length;
   private Color color;
   private int counter;
   public final int x[] = new int[999];
   public final int y[] = new int[999];

   // x and y are head's starting position
    public Snake(int x, int y, char direction, Color c){
        score = 0;
        direction = direction;
        ate = false;
        counter = 3;
        length = 3;
        color = c;
    }
    public void drawSnake(Graphics g) {
        g.setColor(color);
        for(int n = 0; n <= length - 1; n++){
            g.fillRect(x[n] + border, y[n] + border, side - border*2 , side - border*2);
        }
    }
    //moving changes direction and places the snake's head in the correct
    //direction
    public void move() {
        int tempx = x[0];
        int tempy = y[0];
        for (int z = length; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        if (direction == 'u'){
            tempy -= 20;
        }
        if (direction == 'd'){
            tempy += 20;
        }
        if (direction == 'l'){
            tempx -= 20;
        }
        if (direction == 'r'){
            tempx += 20;
        }
        x[0] = tempx;
        y[0] = tempy;
    }
    public void grow(Food f){
        if(counter < 3){
            length++;
            counter--;
        }
        if((x[0] == f.x)&&(y[0] == f.y)){
            ate = true;
            f.isEaten = true;
            length++;
            counter--;
        }
        if(counter == 0){
            ate = false;
            counter = 3;
            score++;
        }
    }
}
