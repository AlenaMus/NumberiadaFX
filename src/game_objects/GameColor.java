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
    public static final int MarkerMove = 7;
    public static final int GRAY = 0;
    public static final int MARKER = 100;

    public static String setColor(int color)
    {
        String colorToSet="button-gray";

        switch (color)
        {
            case RED: colorToSet = "button-red";
                break;
            case BLUE: colorToSet = "button-blue";
                break;
            case GREEN:colorToSet = "button-green";
                break;
            case YELLOW:colorToSet = "button-yellow";
                break;
            case PURPLE:colorToSet = "button-purple";
                break;
            case PINK:colorToSet = "button-pink";
                break;
            case GRAY:colorToSet = "button-gray";
                break;
            case MarkerMove: colorToSet = "button-move";
                break;
            case MARKER:colorToSet = "button-marker";
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
            case PINK: color = "PINK";
                break;
        }
        return color;

    }








}
