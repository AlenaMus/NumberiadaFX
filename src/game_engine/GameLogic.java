package game_engine;

import game_objects.*;
import game_validation.ValidationResult;
import game_validation.XmlNotValidException;
import jaxb.schema.generated.GameDescriptor;
import jaxb.schema.generated.Range;
import jaxb.schema.generated.Squares;

import java.util.ArrayList;
import java.util.List;


public abstract class GameLogic {

    public static final int HUMAN_PLAYER = 1;
    public static final int COMPUTER_PLAYER =2;
    public static final int COMPUTERS_GAME =3;
    public static final int BAD_SQUARE = 100;

    protected String gameFile = " ";
    public  boolean isEndOfGame = false;
    private int gameMoves=0;

    public static ValidationResult validationResult;
    protected List<Square> explicitSquares = new ArrayList<Square>();
    protected List<Player>  players = new ArrayList<>();
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
    public Board getGameBoard() {return gameBoard;}
    public void setGameBoard(Board gameBoard) {
        this.gameBoard = gameBoard;
    }
    public int getMoves() {return gameMoves;}
    public List<Player>  getPlayers(){return players;}

    public abstract void initGame();
    public abstract int updateBoard(Point squareLocation);
    public abstract void makeMove();
    public abstract boolean InitMoveCheck();
   // public abstract void setBoard(jaxb.schema.generated.Board board);
    public abstract void FillRandomBoard();
    public abstract void checkXMLData(GameDescriptor loadedGame)throws XmlNotValidException;
    public abstract void checkRandomBoardValidity(Range boardRange, int boardSize)throws XmlNotValidException;
    public abstract void checkExplicitBoard(List<jaxb.schema.generated.Square> squares, jaxb.schema.generated.Marker marker, int boardSize)throws XmlNotValidException;
    public abstract void gameOver();
    protected abstract void switchPlayer();


    public void setPlayers(jaxb.schema.generated.Players gamePlayers) //after being checked
    {
        for (jaxb.schema.generated.Player player:gamePlayers.getPlayer()) {
            int id = player.getId().intValue();
            String name = player.getName();
            ePlayerType playerType = ePlayerType.valueOf(player.getType());
            int color = player.getColor();
            game_objects.Player player1 = new game_objects.Player(playerType,name,id,color);
            players.add(player1);
        }
    }

    public boolean makeHumanMove(Point userPoint) //GET POINT FROM UI
    {
        boolean IsValidMove;
        int squareValue;
        squareValue = updateBoard(userPoint); //update 2 squares
        if (squareValue == BAD_SQUARE)
            IsValidMove = false;
        else {
            updateUserData(squareValue);
            gameBoard.getMarker().setMarkerLocation(userPoint.getRow(), userPoint.getCol());
            IsValidMove = true;
        }
        return IsValidMove;
    }

    /*protected int updateBoard(Point squareLocation) //implement in Board - returns updated value of row/column
    {
        int squareValue = gameBoard.updateBoard(squareLocation);
        return squareValue;
    }*/

    protected void updateUserData(int squareValue) // in Player?
    {
        currentPlayer.setNumOfMoves(currentPlayer.getNumOfMoves()+1); //maybe do totalmoves var in gameManager
        currentPlayer.setScore(squareValue);
    }


    public void checkBoardXML(jaxb.schema.generated.Board board)throws XmlNotValidException
    {

        eBoardType boardType;
        int size = board.getSize().intValue();

        if((size >= game_objects.Board.MIN_SIZE )&& (size <= game_objects.Board.MAX_SIZE)) {
            boardType = eBoardType.valueOf(board.getStructure().getType());

            switch (boardType) {
                case Random:
                    Range range = board.getStructure().getRange();
                    try {
                        checkRandomBoardValidity(range, size);
                    }
                    catch (XmlNotValidException ex)
                    {
                        validationResult.add("Random Board Validation Failed!");
                        throw new XmlNotValidException(validationResult);
                    }
                    break;
                case Explicit:
                    explicitSquares.clear();
                    Squares square = board.getStructure().getSquares();
                    try {
                        checkExplicitBoard(square.getSquare(), square.getMarker(), size);
                    }
                    catch (XmlNotValidException ex)
                    {
                        validationResult.add("Random Board Validation Failed!");
                        throw new XmlNotValidException(validationResult);
                    }
                    break;
            }
        }
    }

    protected void checkAndSetPlayersXML(jaxb.schema.generated.Players players)throws XmlNotValidException{}

    public boolean isInBoardRange(int num, int size)
    {
        boolean isValid = true;
        if(num < 0 || num > size)
        {
            isValid = false;
        }
        return isValid;
    }

    public void loadDataFromJaxbToGame(GameDescriptor loadedGame,String gameType) {

       // explicitSquares.clear();
        setGameType(eGameType.valueOf(gameType));
        jaxb.schema.generated.Board loadedBoard = loadedGame.getBoard();
        setBoard(loadedBoard);
    }

    public void setBoard(jaxb.schema.generated.Board board)
    {
        eBoardType boardType = eBoardType.valueOf(board.getStructure().getType());
        gameBoard = new Board(board.getSize().intValue(),boardType);

        switch (boardType) {
            case Explicit: Point markerLocation = new Point(board.getStructure().getSquares().getMarker().getRow().intValue(),board.getStructure().getSquares().getMarker().getColumn().intValue());
                FillExplicitBoard(explicitSquares,markerLocation);
                break;
            case Random:
                BoardRange range = new BoardRange(board.getStructure().getRange().getFrom(),board.getStructure().getRange().getTo());
                gameBoard.setBoardRange(range);
                FillRandomBoard();
                break;
        }
    }


    public void FillExplicitBoard(List<Square> xmlBoardList,Point markerLocation)
    {
        int col ,row,color;
        String val;
        Square[][] board = gameBoard.getGameBoard();

        for(Square square:xmlBoardList)
        {
            col = square.getLocation().getCol();
            row = square.getLocation().getRow();
            val = square.getValue();
            color = square.getColor();

            board[row-1][col-1].setColor(color);
            board[row-1][col-1].setValue(val);
        }

        gameBoard.getMarker().setMarkerLocation(markerLocation.getRow(),markerLocation.getCol());
        board[markerLocation.getRow()-1][markerLocation.getCol()-1].setValue(gameBoard.getMarker().getMarkerSign());

    }

}
