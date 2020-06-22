import java.awt.*;
import java.util.Formatter;

public class Food
{
    private final int RANGEX = 59;
    private final int RANGEY = 34;
    public int x,y;
    private final int border = 2;
    public boolean isEaten;
    /**
    * Constructor for objects of class Food
    */
    public Food()
    {
        x = (int) Math.floor(Math.random() * RANGEX)*20;
        y = (int) Math.floor(Math.random() * RANGEY)*20;
        isEaten = false;
    }
    public int getRangeX(){
        return RANGEX;
    }
    public int getRangeY(){
        return RANGEY;
    }
    public void drawFood(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x + border, y + border, 20 - border*2, 20- border*2);
    }
}
