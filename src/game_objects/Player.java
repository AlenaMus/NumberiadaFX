package game_objects;

import game_engine.eTurn;
import javafx.beans.property.*;

public class Player implements Comparable<Player> {

    protected StringProperty name;
    protected IntegerProperty id;
    protected StringProperty playerID;
    protected eTurn turn;
    protected IntegerProperty score;
    protected int numOfMoves;
    private IntegerProperty color;
    private StringProperty playerColor;
    private BooleanProperty IsActive;
    private StringProperty playerType;
    private StringProperty scoreString;

    public String getScoreString() {
        return scoreString.get();
    }

    public StringProperty scoreStringProperty() {
        return scoreString;
    }

    public void setScoreString(String scoreString) {
        this.scoreString.set(scoreString);
    }

    public Player(ePlayerType playerType1, String playerName, int playerId, int color1)
    {
        this();
        name.setValue(playerName);
        id.setValue(playerId);
        playerType.setValue(String.valueOf(playerType1));
        color.setValue(color1);
        playerColor.setValue(GameColor.getColor(color1));
        score.setValue(0);
        numOfMoves = 0;

    }


    public Player(Player player){
        this();
        name.setValue(player.getName());
        id.setValue(player.getId());
        playerType.setValue(String.valueOf(player.getPlayerType()));
        color.setValue(player.getColor());
        playerColor.setValue(GameColor.getColor(player.getColor()));
        scoreString.set(player.scoreStringProperty().get());
        this.scoreProperty().set(player.scoreProperty().get());
        numOfMoves = player.getNumOfMoves();
    }


    public Player(eTurn turn,ePlayerType playerType1,int score)
    {
        IsActive = new SimpleBooleanProperty(true);
        this.turn = turn;
        this.score = new SimpleIntegerProperty(score);
        numOfMoves = 0;
        name = new SimpleStringProperty(String.valueOf(turn));
        playerType = new SimpleStringProperty(String.valueOf(playerType1));
        scoreString = new SimpleStringProperty(String.valueOf(score));
    }





    public Player()
    {
        IsActive = new SimpleBooleanProperty(true);
        name = new SimpleStringProperty();
        id = new SimpleIntegerProperty();
        playerType = new SimpleStringProperty();
        color = new SimpleIntegerProperty();
        playerColor = new SimpleStringProperty();
        score = new SimpleIntegerProperty(0);
        scoreString = new SimpleStringProperty("0");
        numOfMoves = 0;


    }

    public StringProperty nameProperty() {return name;}
    public StringProperty playerTypeProperty() {return playerType;}

    public void setPlayerType(String playerType) {this.playerType.set(playerType);}
    public String getPlayerType() {return playerType.get();}
    public int getScore(){return score.get();}
    public void setScore(int score) {
        this.score.setValue(score);
    }

    public String getPlayerColor() {return playerColor.get();}
    public StringProperty playerColorProperty() {return playerColor;}
    public void setPlayerColor(String playerColor) {this.playerColor.set(playerColor);}
    public IntegerProperty colorProperty() {return color;}
    public void setColor(int color) {this.color.set(color);}
    public int getColor()
    {
        return color.get();
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
            if(newPlayer.getId() == id.get() || newPlayer.color.get() == this.color.get())
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

    @Override
    public int compareTo(Player player) {
        return this.getScore()-player.getScore();
    }
}
