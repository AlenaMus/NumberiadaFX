package game_objects;

import game_engine.eTurn;
import javafx.beans.property.*;
import javafx.scene.paint.Color;

public class Player {

    protected StringProperty name;
    protected IntegerProperty id;
    protected StringProperty playerID;
    protected eTurn turn;
    protected IntegerProperty score;

    public String getPlayerID() {
        return playerID.get();
    }



    public void setPlayerID(String playerID) {
        this.playerID.set(playerID);
    }

    protected int numOfMoves;
    private IntegerProperty color;
    private StringProperty playerColor;

    public void setPlayerColor(String playerColor) {
        this.playerColor.set(playerColor);
    }

    private BooleanProperty IsActive;
    private StringProperty scoreString;
    private StringProperty playerType;
    private ePlayerType playerEtype;


    public Player(ePlayerType playerType1, String playerName, int playerId, int color1)
    {
        this();
        playerID.setValue(String.valueOf(playerId));
        scoreString.setValue(String.valueOf(score.get()));
        name.setValue(playerName);
        id.setValue(playerId);
        playerType.setValue(String.valueOf(playerType1));
        color.setValue(color1);
        playerColor.setValue(String.valueOf(color1));
        score.setValue(0);
        scoreString.setValue(String.valueOf(score));
        numOfMoves = 0;
        this.playerEtype = playerType1;

    }

    public Player(eTurn turn,ePlayerType playerType1)
    {
        IsActive = new SimpleBooleanProperty(true);
        this.turn = turn;
        score = new SimpleIntegerProperty(0);
        numOfMoves = 0;
        name = new SimpleStringProperty("");
        playerType = new SimpleStringProperty(String.valueOf(playerType1));
        this.playerEtype = playerType1;

    }


    public Player()
    {
        playerID = new SimpleStringProperty();
        scoreString = new SimpleStringProperty();
        IsActive = new SimpleBooleanProperty(true);
        name = new SimpleStringProperty();
        id = new SimpleIntegerProperty();
        playerType = new SimpleStringProperty();
        color = new SimpleIntegerProperty();
        playerColor = new SimpleStringProperty();
        score = new SimpleIntegerProperty(0);
        numOfMoves = 0;


    }

    public StringProperty nameProperty() {return name;}
    public StringProperty playerTypeProperty() {return playerType;}
    public StringProperty playerIDProperty() {
        return playerID;
    }

    public void setPlayerType(String playerType) {
        this.playerType.set(playerType);
    }



    public int getScore(){return score.get();}
    public void setScore(int addScore) {
        int newScore = score.get()+addScore;
        score.setValue(newScore);
        scoreString.setValue(String.valueOf(score.get()));
    }

    public String getPlayerColor() {return playerColor.get();}
    public StringProperty playerColorProperty() {return playerColor;}


    public IntegerProperty colorProperty() {return color;}
    public void setColor(int color) {this.color.set(color);}

    public String getScoreString() {return scoreString.get();}
    public StringProperty scoreStringProperty() {return scoreString;}
    public void setScoreString(String scoreString) {this.scoreString.setValue(scoreString);}

    public ePlayerType getPlayerEtype() {
        return playerEtype;
    }

    public void setPlayerEtype(ePlayerType playerEtype) {
        this.playerEtype = playerEtype;
    }

    public IntegerProperty idProperty() {return id;}
    public IntegerProperty scoreProperty() {return score;}

    public boolean isActive() {
        return IsActive.get();
    }
    public void setActive(boolean active) {IsActive.setValue(active);}

    public int getId() {
        return id.get();
    }
    public void setId(int playerID) { id.setValue(playerID);}

    public ePlayerType getPlayerType() {
        return playerEtype;
    }
    public void setPlayerType(ePlayerType playerType) {
        this.playerEtype = playerType;
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
