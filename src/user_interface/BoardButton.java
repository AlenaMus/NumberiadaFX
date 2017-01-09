package user_interface;

import game_objects.GameColor;
import game_objects.Marker;
import game_objects.Point;
import game_objects.Square;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class BoardButton extends Button {

    private Square boardButton;


public BoardButton(Square buttonSquare)
{

    this.boardButton = buttonSquare;
    if(boardButton.getValue().isEmpty())
    {
        this.setButtonColor(GameColor.GRAY);
        //this.disableProperty().setValue(true);
    }
    else if(boardButton.getValue().equals(Marker.markerSign)) {
        this.setText(boardButton.getValue());
        this.setId(Marker.markerStyle);
        this.getStyleClass().add(Marker.markerStyle);
    }else{
        this.setText(boardButton.getValue());
        this.setButtonColor(buttonSquare.getColor());
    }


}


public Point getLocation()
{
    return boardButton.getLocation();
}

public int getValue()
{
    return Integer.valueOf(boardButton.getValue());
}

public void setButtonColor(int color)
 {
        String setColor = GameColor.setColor(color);
        this.setId(setColor);
        this.getStyleClass().add(setColor);

 }




}
