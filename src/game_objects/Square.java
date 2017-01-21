/**
 * Created by Alona on 11/7/2016.
 */

package game_objects;


import javafx.beans.property.*;

public class Square {

    private Point location;
    private StringProperty squareValue;
    private BooleanProperty isDisabled;
    private IntegerProperty color;
    private BooleanProperty isEmpty;
    private BooleanProperty setEffect;

    public Square(Point location) {
        this.location = location;
        initSquare();
        isEmpty.setValue(true);
        squareValue.setValue("");
        color.setValue(GameColor.GRAY);
        isDisabled.setValue(false);
    }

    public Square(){
        initSquare();
        isEmpty.setValue(true);
    }

    public Square (Square copySquare){
        initSquare();
        this.location = new Point(copySquare.getLocation().getRow(),copySquare.getLocation().getCol());
        this.colorProperty().set(copySquare.colorProperty().get());
        this.squareValueProperty().set(copySquare.getSquareValue());
        this.isDisabled.set(copySquare.isDisabled());
        this.setIsEmpty(copySquare.isEmpty());
    }

    public void initSquare()
    {
        squareValue = new SimpleStringProperty();
        isDisabled = new SimpleBooleanProperty();
        isEmpty = new SimpleBooleanProperty();
        color = new SimpleIntegerProperty();
        setEffect = new SimpleBooleanProperty(false);
    }

    public boolean isSetEffect() {
        return setEffect.get();
    }

    public BooleanProperty setEffectProperty() {
        return setEffect;
    }

    public void setSetEffect(boolean setEffect) {
        this.setEffect.set(setEffect);
    }

    public int getColor() {
        return color.get();
    }

    public IntegerProperty colorProperty() {
        return color;
    }

    public void setColor(int color1) {
        color.set(color1);
    }


    public Square(Point location, String value, int color1)
    {
        initSquare();
        squareValue.setValue(value);
        this.location = new Point(location.getRow(),location.getCol());
        isDisabled.setValue(false);
        isEmpty.setValue(false);
        color.setValue(color1);

    }

    public String getSquareValue() {
        return squareValue.get();
    }

    public StringProperty squareValueProperty() {
        return squareValue;
    }

    public void setSquareValue(String squareValue) {
        this.squareValue.set(squareValue);
    }

    public boolean isIsDisabled() {
        return isDisabled.get();
    }

    public BooleanProperty isDisabledProperty() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled.set(isDisabled);
    }


    public boolean isIsEmpty() {
        return isEmpty.get();
    }

    public BooleanProperty isEmptyProperty() {
        return isEmpty;
    }

    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty.set(isEmpty);
    }

    public boolean isDisabled() {return isDisabled.get();}
    public void setDisabled(boolean disabled) {isDisabled.setValue(disabled);}

    public Point getLocation() {
        return location;
    }
    public void setLocation(Point location) {
        this.location = location;
    }

    public boolean isEmpty() {return isEmpty.get();}
    public void setEmpty(boolean empty) {
        isEmpty.setValue(empty);
    }

    public String getValue(){return squareValue.getValue();}
    public void setValue(String value)
    {
        squareValue.setValue(value);
        if(value.equals(""))
        {
            this.setEmpty(true);
        }
        else
        {
            this.setEmpty(false);
            squareValue.setValue(value);
        }
    }


    public static int ConvertFromStringToIntValue(String value)
    {
        int num = 0;
        if(value != null && !value.equals(Marker.markerSign))
        {
            num = Integer.parseInt(value);
        }

        return num;

    }

    public static String ConvertFromIntToStringValue(int value)
    {
        String valueS = "";
        if(value > 0)
        {
            valueS=Integer.toString(value);
        }
        else
        {
            valueS =Integer.toString(value);
        }

        return valueS;
    }

    public boolean isMarker(String value)
    {
        boolean isMarker = value.equals(Marker.markerSign)?true:false;
        return isMarker;
    }


    @Override
    public boolean equals(Object square)
    {
        boolean isEqual = false;
        if(square != null) {
            if (square instanceof Square) {
                  Square square1 = (Square) square;
                if (this.getLocation().getCol() == square1.getLocation().getCol() &&
                        this.getLocation().getRow() == square1.getLocation().getRow()&&
                        this.getColor() == square1.getColor()) {
                    isEqual = true;
                }
            }
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return super.hashCode();

    }




}
