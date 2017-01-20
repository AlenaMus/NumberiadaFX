package user_interface;

import game_objects.GameColor;
import game_objects.Marker;
import game_objects.Point;
import game_objects.Square;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class BoardButton extends Button {

    private Square boardButton;
    private IntegerProperty buttonColor;

    public int getButtonColor() {
        return buttonColor.get();
    }

    public IntegerProperty buttonColorProperty() {
        return buttonColor;
    }

    public void setButtonColor(int buttonColor) {
        this.buttonColor.set(buttonColor);
        String setColor = GameColor.setColor(buttonColor);
        this.setId(setColor);
        this.getStyleClass().add(setColor);
    }


    public void setBColor(int color)
    {
        String setColor = GameColor.setColor(color);
        this.setId(setColor);
        this.getStyleClass().add(setColor);
        buttonColor.set(color);

    }


    public BoardButton(Square buttonSquare) {

         buttonColor = new SimpleIntegerProperty();
         this.boardButton = buttonSquare;

    if(boardButton.getValue().isEmpty())
    {
        this.setButtonColor(GameColor.GRAY);
    }
    else if(boardButton.getValue().equals(Marker.markerSign)) {
          this.setText(boardButton.getValue());
          setButtonColor(buttonSquare.getColor());
    }
    else{
         this.setText(boardButton.getValue());
         setButtonColor(buttonSquare.getColor());
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





}
