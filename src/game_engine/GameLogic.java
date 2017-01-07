package game_engine;

import game_objects.*;

import game_objects.Board;
import game_objects.Player;
import game_validation.ValidationResult;
import game_validation.XmlNotValidException;
import jaxb.schema.generated.*;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class GameLogic {

    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 6;

    public static final int HUMAN_PLAYER = 1;
    public static final int COMPUTER_PLAYER =2;
    public static final int COMPUTERS_GAME =3;
    public static final int BAD_SQUARE = 100;

    protected String gameFile = " ";
    public  boolean isEndOfGame = false;
    private int gameMoves=0;

    protected Map<Integer, game_objects.Player> players = null;
    protected GameDescriptor loadedGame;
    protected  int numOfPlayers;
    protected game_objects.Board gameBoard;
    protected game_objects.Player currentPlayer;
    private eGameType gameType;



    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public void setCurrentPlayer(game_objects.Player currentPlayer)
    {
        this.currentPlayer = currentPlayer;
    }
    public GameDescriptor getLoadedGame() {
        return loadedGame;
    }
    public void setLoadedGame(GameDescriptor loadedGame) {
        this.loadedGame = loadedGame;
    }
    public void setGameType(eGameType type){gameType = type;}
    public int getNumOfPlayers () {return numOfPlayers;}
    public void setNumOfPlayers(int num) { numOfPlayers = num; }
    public Board getGameBoard() {
        return gameBoard;
    }
    public void setGameBoard(Board gameBoard) {
        this.gameBoard = gameBoard;
    }
    public int getMoves() {return gameMoves;}
    public abstract Map<Integer, Player>  getPlayers();

    public abstract void makeMove();
    public abstract boolean InitMoveCheck();
    public abstract void setBoard(jaxb.schema.generated.Board board);
    public abstract boolean checkXMLData(GameDescriptor loadedGame);
    public abstract boolean checkRandomBoardValidity(Range boardRange, int boardSize);
    public abstract boolean checkExplicitBoard(List<jaxb.schema.generated.Square> squares, jaxb.schema.generated.Marker marker, int boardSize);
    public abstract void gameOver();
    protected abstract void switchPlayer();





    public void setPlayers(jaxb.schema.generated.Players gamePlayers) //after being checked
    {
       players = new HashMap<>(numOfPlayers);
        for (jaxb.schema.generated.Player player:gamePlayers.getPlayer()) {
            int id = player.getId().intValue();
            String name = player.getName();
            ePlayerType playerType = ePlayerType.valueOf(player.getType());
            int color = player.getColor();
            game_objects.Player player1 = new game_objects.Player(playerType,name,id,color);
            players.put(id,player1);
        }
    }




    protected int updateBoard(Point squareLocation) //implement in Board - returns updated value of row/column
    {
        int squareValue = gameBoard.updateBoard(squareLocation);
        return squareValue;
    }


    protected void updateUserData(int squareValue) // in Player?
    {
        currentPlayer.setNumOfMoves(currentPlayer.getNumOfMoves()+1); //maybe do totalmoves var in gameManager
        currentPlayer.setScore(squareValue);
    }


    public boolean checkBoardXML(jaxb.schema.generated.Board board)
    {

        boolean isValidBoard = true;
        eBoardType boardType;
        int size = board.getSize().intValue();

        if((size >= game_objects.Board.MIN_SIZE )&& (size <= game_objects.Board.MAX_SIZE)) {
            boardType = eBoardType.valueOf(board.getStructure().getType());

            switch (boardType) {
                case Random:
                    Range range = board.getStructure().getRange();
                    isValidBoard = checkRandomBoardValidity(range, size);
                    break;
                case Explicit:
                    Squares square = board.getStructure().getSquares();
                    isValidBoard = checkExplicitBoard(square.getSquare(), square.getMarker(), size);
                    break;
            }
        }
        else{
            isValidBoard = false;
        }

        return isValidBoard;
    }

    protected boolean checkAndSetPlayersXML(jaxb.schema.generated.Players players)
    {
        return true;
    }

    public boolean isInBoardRange(int num, int size)
    {
        boolean isValid = true;
        if(num < 0 || num > size)
        {
            isValid = false;
        }
        return isValid;
    }

}
