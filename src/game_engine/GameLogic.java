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

    /*public static final int HUMAN_PLAYER = 1;
    public static final int COMPUTER_PLAYER =2;
    public static final int COMPUTERS_GAME =3;*/
    //public static final int BAD_SQUARE = 100;

    public static final int GOOD_POINT = 1000;
    public static final int NOT_IN_MARKER_ROW_AND_COLUMN = 1001;
    public static final int NOT_PLAYER_COLOR =1002;

    public  boolean isEndOfGame = false;
    private int gameMoves=0;

    public static ValidationResult validationResult;
    protected List<Square> explicitSquares = new ArrayList<Square>();
    protected List<Player>  players = new ArrayList<>();
    protected GameDescriptor loadedGame;
    protected  int numOfPlayers;
    protected game_objects.Board gameBoard;
    protected game_objects.Player currentPlayer = new Player();
    private eGameType gameType;



    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public void setCurrentPlayer(game_objects.Player currentPlayer)
    {
        //this.currentPlayer = currentPlayer;
        this.currentPlayer.setName(currentPlayer.getName());
        this.currentPlayer.idProperty().set(currentPlayer.idProperty().get());
        this.currentPlayer.playerIDProperty().set(currentPlayer.playerIDProperty().get());

        this.currentPlayer.playerColorProperty().set(currentPlayer.playerColorProperty().get());
        this.currentPlayer.colorProperty().set(currentPlayer.colorProperty().get());
        this.currentPlayer.setActive(currentPlayer.isActive());
        this.currentPlayer.setPlayerType(currentPlayer.getPlayerType());
        this.currentPlayer.playerTypeProperty().set(currentPlayer.playerTypeProperty().get());
        this.currentPlayer.scoreStringProperty().set(currentPlayer.scoreStringProperty().get());
        this.currentPlayer.setScore(currentPlayer.getScore());

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

    public abstract void makeComputerMove();
    public abstract void playerRetire();
    public abstract void initGame();
    public abstract int updateBoard(Point squareLocation);
    public abstract void makeHumanMove(Point userPoint); //GET POINT FROM UI
    public abstract int isValidPoint(Point squareLocation);
    public abstract void makeMove();
    public abstract boolean InitMoveCheck();
   // public abstract void setBoard(jaxb.schema.generated.Board board);
    public abstract void FillRandomBoard();
    public abstract void checkXMLData(GameDescriptor loadedGame)throws XmlNotValidException;
    public abstract void checkRandomBoardValidity(Range boardRange, int boardSize)throws XmlNotValidException;
    public abstract void gameOver();
    public abstract boolean switchPlayer();
    protected void checkAndSetPlayersXML(jaxb.schema.generated.Players players)throws XmlNotValidException{}
    public abstract boolean isGameOver();



//    public void setPlayers(jaxb.schema.generated.Players gamePlayers)
//    {
//        for (jaxb.schema.generated.Player player:gamePlayers.getPlayer()) {
//            int id = player.getId().intValue();
//            String name = player.getName();
//            ePlayerType playerType = ePlayerType.valueOf(player.getType());
//            int color = player.getColor();
//            game_objects.Player player1 = new game_objects.Player(playerType,name,id,color);
//            players.add(player1);
//        }
//    }

   /* public int makeHumanMove(Point userPoint) //GET POINT FROM UI
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
    }*/

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


    protected void checkBoardXML(jaxb.schema.generated.Board board)throws XmlNotValidException
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


    protected void checkExplicitBoard(List<jaxb.schema.generated.Square> squares, jaxb.schema.generated.Marker marker, int boardSize) throws XmlNotValidException
    {
        game_objects.Square newSquare;
        int row,col,val,color;
        explicitSquares.clear();

        if(!isInBoardRange(marker.getRow().intValue(),boardSize))
        {
            validationResult.add(String.format("Explicit Board validation : invalid row of marker location ! Must be in range  from %d  to %d",1,boardSize));
            throw new XmlNotValidException(validationResult);
        }
        if(!isInBoardRange(marker.getColumn().intValue(),boardSize))
        {
            validationResult.add(String.format("Explicit Board validation error: invalid column of marker location ! Must be in range  from %d  to %d",1,boardSize));
            throw new XmlNotValidException(validationResult);
        }

        for (jaxb.schema.generated.Square square : squares) {
            row = square.getRow().intValue();
            col = square.getColumn().intValue();
            val = square.getValue().intValue();
            color = square.getColor();

            if ((val < BoardRange.MIN_BOARD_RANGE )|| (val > BoardRange.MAX_BOARD_RANGE)) {
                validationResult.add("Explicit Board Validation Error: squares values must be in between -99 and 99" );
                throw new XmlNotValidException(validationResult);
            }

            if(!(row == marker.getRow().intValue() && col == marker.getColumn().intValue())) {

                if (isInBoardRange(row, boardSize) && isInBoardRange(col, boardSize)) //location is ok
                {
                    newSquare = new game_objects.Square(new Point(row, col), String.valueOf(val), color);
                    if (explicitSquares.contains(newSquare)) {
                        validationResult.add(String.format("Explicit Board validation error: square double location [%d,%d] existance!",
                                square.getRow().intValue(), square.getColumn().intValue()));
                        throw new XmlNotValidException(validationResult);

                    } else {
                        explicitSquares.add(newSquare);
                    }
                } else {
                    explicitSquares.clear();
                    validationResult.add(String.format("Explicit Board Validation Error: Square row :%d,column:%d outside board size :%d", row, col, boardSize));
                    throw new XmlNotValidException(validationResult);
                }
            }else{
                validationResult.add(String.format("Explicit Board Validation Error: Square row :%d,column:%d is both @ marker location and play square location!",
                        row, col));
                throw new XmlNotValidException(validationResult);
            }
        }
    }


    public void loadDataFromJaxbToGame(GameDescriptor loadedGame,String gameType) {
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
