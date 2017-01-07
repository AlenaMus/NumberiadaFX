package game_objects;
import javafx.scene.paint.Color;
import user_interface.BoardButton;

public class GameColor {

    public static final int RED = 1;
    public static final int BLUE = 2;
    public static final int GREEN = 3;
    public static final int YELLOW = 4;
    public static final int PURPLE = 5;
    public static final int PINK = 6;
    public static final int GRAY = 0;

    public static Color setColor(int color)
    {
        Color colorToSet = Color.BLACK;

        switch (color)
        {
            case RED: colorToSet = Color.RED;
                break;
            case BLUE: colorToSet = Color.BLUE;
                break;
            case GREEN:colorToSet = Color.GREEN;
                break;
            case YELLOW:colorToSet = Color.YELLOW;
                break;
            case PURPLE:colorToSet = Color.PURPLE;
                break;
            case PINK:colorToSet = Color.PINK;
                break;
            case GRAY:colorToSet = Color.GRAY;
                break;
        }
        return colorToSet;

    }

    public static String getColor(int color1)
    {
       String color="GRAY";

        switch (color1)
        {
            case RED: color = "RED";
                break;
            case BLUE: color = "BLUE";
                break;
            case GREEN: color = "GREEN";
                break;
            case YELLOW: color = "YELLOW";
                break;
            case PURPLE :color = "PURPLE";
                break;
            case PINK: color ="PINK";
                break;
        }
        return color;

    }








}
