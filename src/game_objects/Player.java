package game_objects;

import game_engine.eTurn;
import javafx.beans.property.*;
import javafx.scene.paint.Color;

public class Player {

    protected StringProperty name;
    protected IntegerProperty id;
    private ePlayerType playerType;
    protected eTurn turn;
    protected IntegerProperty score;
    protected int numOfMoves;
    private IntegerProperty color;
    private StringProperty playerColor;
    private int serialNumber;
    private BooleanProperty IsActive;
    private StringProperty scoreString;

    public StringProperty nameProperty() {return name;}
    public IntegerProperty idProperty() {return id;}
    public IntegerProperty scoreProperty() {return score;}
    public int getScore(){return score.get();}
    public void setScore(int addScore) {
        int newScore = score.get()+addScore;
        score.setValue(newScore);
        scoreString.setValue(String.valueOf(score.get()));
    }
    public String getPlayerColor() {return playerColor.get();}
    public StringProperty playerColorProperty() {return playerColor;}
    public boolean isIsActive() {return IsActive.get();}
    public BooleanProperty isActiveProperty() {return IsActive;}
    public void setIsActive(boolean isActive) {this.IsActive.set(isActive);}
    public IntegerProperty colorProperty() {return color;}
    public void setColor(int color) {this.color.set(color);}
    public String getScoreString() {return scoreString.get();}
    public StringProperty scoreStringProperty() {return scoreString;}
    public void setScoreString(String scoreString) {this.scoreString.setValue(scoreString);}

    public Player(ePlayerType playerType, String playerName, int playerId, int color1)
    {
        scoreString = new SimpleStringProperty();
        IsActive = new SimpleBooleanProperty(true);
        name = new SimpleStringProperty();
        name.setValue(playerName);
        id = new SimpleIntegerProperty(playerId);
        //id.setValue(playerId);
        this.playerType = playerType;
        color = new SimpleIntegerProperty(color1);
        //color.setValue(color1);
        playerColor = new SimpleStringProperty(GameColor.setColor(color1));
        //playerColor.setValue(GameColor.setColor(color1));
        score = new SimpleIntegerProperty(0);
        score.setValue(0);
        scoreString.setValue(String.valueOf(score.get()));
        scoreString.setValue(score.toString());
        numOfMoves = 0;

    }

    public Player(eTurn turn,ePlayerType playerType)
    {
        this.turn = turn;
        this.playerType = playerType;
        score = new SimpleIntegerProperty();
        score.setValue(0);
        numOfMoves = 0;
        name = new SimpleStringProperty();
        name.setValue("");

    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public boolean isActive() {
        return IsActive.get();
    }

    public void setActive(boolean active) {IsActive.setValue(active);}

    public int getId() {
        return id.get();
    }

    public void setId(int playerID) { id.setValue(playerID);}

    public ePlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(ePlayerType playerType) {
        this.playerType = playerType;
    }

    public eTurn getTurn() {
        return turn;
    }

    public void setTurn(eTurn turn) {
        this.turn = turn;
    }



    public int getNumOfMoves() {
        return numOfMoves;
    }

    public void setNumOfMoves(int numOfMoves) {
        this.numOfMoves = numOfMoves;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name1) {
        name.setValue(name1);
    }

    public int getColor()
    {
        return color.get();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object player) {
        boolean isEqual = false;
        Player newPlayer;

        if(player instanceof Player)
        {
            newPlayer = (Player)player;
            if(newPlayer.getId() == id.get() || newPlayer.color == this.color)
            {
                isEqual = true;
            }
        }
        return isEqual;
    }

    public boolean checkPlayerTurn(Player currentPlayer) //only for basic game use //
    {
       boolean isCurrentPlayer = false;

        if(currentPlayer != null)
        {
            if(this.turn == currentPlayer.turn)
            {
                isCurrentPlayer = true;
            }
        }
        return isCurrentPlayer;
    }
}
