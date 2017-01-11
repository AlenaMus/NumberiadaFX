package game_engine;

import game_objects.BoardRange;
import game_objects.GameColor;
import game_objects.Point;
import game_objects.ePlayerType;
import game_validation.ValidationResult;
import game_validation.XmlNotValidException;
import jaxb.schema.generated.GameDescriptor;
import jaxb.schema.generated.Player;
import jaxb.schema.generated.Range;
import jaxb.schema.generated.Square;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class AdvancedGame extends GameLogic{

    public static final int MIN_PLAYERS = 3;
    public static final int MAX_PLAYERS = 6;


    @Override
    public void initGame()
    {
        setCurrentPlayer(players.get(0));
    }

    @Override
    public void gameOver()
    {
        Collections.sort(players, (p1, p2) -> p1.getScore() - p2.getScore());
            //players in now sorted by SCORE
            //need to print to UI the players SCORES

    }

    @Override
    public boolean InitMoveCheck()
    {
        return true;
    }


    @Override
    public void makeMove()
    {

    }


/*    @Override
    protected int updateBoard(Point squareLocation) {
            int squareValue = gameBoard.updateBoard(squareLocation);
            return squareValue;

    }*/

    @Override
    protected void updateUserData(int squareValue) {
        currentPlayer.setNumOfMoves(currentPlayer.getNumOfMoves()+1); //maybe do totalmoves var in gameManager
        currentPlayer.setScore(squareValue);
    }

    @Override
    public void switchPlayer()
    {
        int activePlayerNumber = currentPlayer.getSerialNumber();
        game_objects.Player nextPlayer = null;
        if(isGameOver2(gameBoard.getMarker().getMarkerLocation()))
        {
            gameOver();
        }
        else //somebody have move
            nextPlayer = players.get((activePlayerNumber+1) % gameBoard.GetBoardSize());
        while (!nextPlayer.isActive())
        {
            nextPlayer = players.get((activePlayerNumber+1)%gameBoard.GetBoardSize());
        }
        while (!(isPlayerHaveMove(gameBoard.getMarker().getMarkerLocation(),nextPlayer)))
        {
            //need to update UI player have no move!!!
            nextPlayer = players.get((activePlayerNumber+1)%gameBoard.GetBoardSize());
            while (!nextPlayer.isActive())
            {
                nextPlayer = players.get((activePlayerNumber+1)%gameBoard.GetBoardSize());
            }
        }
        setCurrentPlayer(nextPlayer);
    }

    private boolean isPlayerHaveMove(Point markerLocation, game_objects.Player player)
    {
        int MarkerRow = markerLocation.getRow()-1;
        int MarkerCol = markerLocation.getCol()-1;
        for (int i=0; i < gameBoard.GetBoardSize();i++)
            if ((!gameBoard.getGameBoard()[MarkerRow][i].isDisabled()) &&(!gameBoard.getGameBoard()[MarkerRow][i].isEmpty())
                    && (!gameBoard.getGameBoard()[MarkerRow][i].getValue().equals(gameBoard.getMarker().getMarkerSign()))
                    && ((gameBoard.getGameBoard()[MarkerRow][i].getColor()  == (getCurrentPlayer().getColor()))))
                return true;
        for (int i=0; i < gameBoard.GetBoardSize(); i++)
            if ((!gameBoard.getGameBoard()[i][MarkerCol].isDisabled() )&& (!gameBoard.getGameBoard()[i][MarkerCol].isEmpty() )
                    && (!gameBoard.getGameBoard()[i][MarkerCol].getValue().equals(gameBoard.getMarker().getMarkerSign()))
                    && ((gameBoard.getGameBoard()[i][MarkerCol].getColor()  == (player.getColor()))))
                return true;
        return false;
    }

    private boolean isGameOver2(Point markerLocation)
    {
        int MarkerRow = markerLocation.getRow()-1;
        int MarkerCol = markerLocation.getCol()-1;
        for (int i=0; i < gameBoard.GetBoardSize();i++)
            if ((!gameBoard.getGameBoard()[MarkerRow][i].isDisabled()) &&(!gameBoard.getGameBoard()[MarkerRow][i].isEmpty())
                    && (!gameBoard.getGameBoard()[MarkerRow][i].getValue().equals(gameBoard.getMarker().getMarkerSign())))
                return false;
        for (int i=0; i < gameBoard.GetBoardSize(); i++)
            if ((!gameBoard.getGameBoard()[i][MarkerCol].isDisabled() )&& (!gameBoard.getGameBoard()[i][MarkerCol].isEmpty() )
                    && (!gameBoard.getGameBoard()[i][MarkerCol].getValue().equals(gameBoard.getMarker().getMarkerSign())))
                return false;
        return true;
    }

    private void playerRetire (game_objects.Player player) {
        for (int i = 0; i < gameBoard.GetBoardSize(); i++)
            for (int j = 0; j < gameBoard.GetBoardSize(); j++)
                if (gameBoard.getGameBoard()[i][j].getColor() == player.getColor())
                {
                    gameBoard.getGameBoard()[i][j].setDisabled(true);
                    gameBoard.getGameBoard()[i][j].setValue(" ");
                    gameBoard.getGameBoard()[i][j].setEmpty(true);
                }
        player.setActive(false);
        //NEED TO UPDATE UI and delete all player squars
    }




    public static int ComputerMove(int boardSize) {
        return (ThreadLocalRandom.current().nextInt(1, boardSize + 1));
    }


    private void makeComputerMove()
    {
        int squareValue = BAD_SQUARE ;
        boolean foundSquare = false;
        Point squareLocation = null;
        int MarkerRow = gameBoard.getMarker().getMarkerLocation().getRow()-1;
        int MarkerCol = gameBoard.getMarker().getMarkerLocation().getCol()-1;

        while (!foundSquare) {
            int random = ComputerMove(gameBoard.GetBoardSize());
            if (gameBoard.getGameBoard()[MarkerRow][random].getColor() == (currentPlayer.getColor())) {
                squareLocation = new Point(gameBoard.getMarker().getMarkerLocation().getRow(), random);
                foundSquare = true;
            }
            else if (gameBoard.getGameBoard()[random][MarkerCol].getColor() == (currentPlayer.getColor())) {
                squareLocation = new Point(random, gameBoard.getMarker().getMarkerLocation().getCol());
                foundSquare = true;
            }
        }
        squareValue = updateBoard(squareLocation); //update 2 squares
        updateUserData(squareValue);
        gameBoard.getMarker().setMarkerLocation(squareLocation.getRow(),squareLocation.getCol());
    }

    public boolean makeHumanMove(Point userPoint)
    {
        boolean IsValidMove;
        int squareValue;
        squareValue = updateBoard(userPoint); //update 2 squares
        if (squareValue == GameLogic.BAD_SQUARE)
            IsValidMove = false;
        else {
            updateUserData(squareValue);
            gameBoard.getMarker().setMarkerLocation(userPoint.getRow(), userPoint.getCol());
            IsValidMove = true;
        }
        return IsValidMove;
    }


    public int updateBoard(Point squareLocation)
    {
        int squareValue = 100;
        Point oldMarkerPoint = gameBoard.getMarker().getMarkerLocation();

        if (currentPlayer.getColor() != gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()].getColor())
            return squareValue;
        String squareStringValue = gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()].getValue();//get number
        if (squareStringValue.equals(gameBoard.getMarker().getMarkerSign()) || squareStringValue.isEmpty()) { //checks if wrong square-marker or empty
            return squareValue;
        }
        squareValue = game_objects.Square.ConvertFromStringToIntValue(squareStringValue); //return number value

        gameBoard.getGameBoard()[oldMarkerPoint.getRow()-1][oldMarkerPoint.getCol()-1].setValue("");    //empty old marker location

        gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()].setValue( gameBoard.getMarker().markerSign); //update marker to square

        return squareValue;
    }

    @Override
    public void checkXMLData(GameDescriptor loadedGame)throws XmlNotValidException
    {

        validationResult = new ValidationResult();
        int numOfPlayers= loadedGame.getPlayers().getPlayer().size();

        if(numOfPlayers< MIN_PLAYERS || numOfPlayers> MAX_PLAYERS)
        {
            validationResult.add(String.format("XML Load error: %d is invalid number of players,must be minimum 3 - 6 players",getNumOfPlayers()));
            throw new XmlNotValidException(validationResult);
        }
            setNumOfPlayers(numOfPlayers);
            super.checkBoardXML(loadedGame.getBoard());
            checkAndSetPlayersXML(loadedGame.getPlayers());
    }

    @Override
    public void checkRandomBoardValidity(Range boardRange, int boardSize) throws XmlNotValidException
    {
        int range;

        if(!(boardRange.getFrom() >= BoardRange.MIN_BOARD_RANGE &&  boardRange.getTo() <= BoardRange.MAX_BOARD_RANGE))
        {
            validationResult.add(String.format("Random Board Validation Error: board range have to be in [-99,99] range,range [%d,%d] is invalid ",
                    boardRange.getFrom(),boardRange.getTo()));
            throw new XmlNotValidException(validationResult);
        }
        if(boardRange.getFrom() <= boardRange.getTo())
        {
            range = boardRange.getTo() - boardRange.getFrom() +1;

            if(((boardSize*boardSize -1) / (range*getNumOfPlayers()) == 0))
            {
               validationResult.add(String.format("Random Board Validation Error: Board Size %d < Board Range %d for %d players!",
                   boardSize*boardSize,range,getNumOfPlayers()));
                throw new XmlNotValidException(validationResult);
            }

        } else {
                    validationResult.add(String.format("Random Board Validation Error: Board Range numbers invalid from %d > to %d",
                     boardRange.getFrom(),boardRange.getTo()));
                     throw new XmlNotValidException(validationResult);
        }
    }

    @Override
    public void checkExplicitBoard(List<Square> squares, jaxb.schema.generated.Marker marker, int boardSize) throws XmlNotValidException
    {
        game_objects.Square newSquare;
        int row,col,val,color;

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



    @Override
    public void checkAndSetPlayersXML(jaxb.schema.generated.Players players)throws XmlNotValidException {
        List<Player> gamePlayers = players.getPlayer();
        game_objects.Player newPlayer;


        for (jaxb.schema.generated.Player player : gamePlayers) {
             newPlayer = new game_objects.Player(ePlayerType.valueOf(player.getType()), player.getName(), player.getId().intValue(), player.getColor());
            if(getPlayers()!=null)
            {
                if (getPlayers().contains(newPlayer)) {
                    validationResult.add(String.format("Player Validation Error: name = %s ,id = %d, color = %s already exists !",
                            player.getName(),player.getId(),player.getColor()));
                    getPlayers().clear();
                    throw new XmlNotValidException(validationResult);
                } else {
                    getPlayers().add(newPlayer);
                }
            }
            else
            {
                getPlayers().add(newPlayer);
            }

        }
    }


    @Override
    public void FillRandomBoard() {

       // List<game_objects.Square> randomList = new ArrayList<>();

        int i ;
        int j ;
        int row =0;
        int col=0;
        int color=1;
        int boardSize = gameBoard.GetBoardSize();
        BoardRange boardRange = gameBoard.getBoardRange();
        game_objects.Square[][] board = gameBoard.getGameBoard();
       // Random rand = new Random();

        // filling our numbers in given range
        int rangeSize = boardRange.RangeSize();
     //   int printNumCount = (boardSize * boardSize -1) / (rangeSize*numOfPlayers); //49/9=5
        int rangeNumToPrint = boardRange.getFrom();


        for(int m = 0;m < rangeSize ;m++) {              ///&& i< boardSize
            for (int k = 0; k < numOfPlayers; k++) {    // && i< boardSize;

                board[row][col].setValue(game_objects.Square.ConvertFromIntToStringValue(rangeNumToPrint));
                board[row][col].setColor(color);
                color++;
                if(col == boardSize-1)
                {
                    col = -1;
                    row++;
                }
                col++;

            }
            rangeNumToPrint++;
            color=1;
        }

        if (col == boardSize) {
            col = 0;
        }


        for (int m = row; m < boardSize; m++) {
            for (int n = col; n < boardSize; n++) {
                board[m][n].setValue("");
                board[m][n].setColor(GameColor.GRAY);
            }
        }

        board[boardSize - 1][boardSize - 1].setValue(game_objects.Marker.markerSign);
        gameBoard.shuffleArray(board);

        String MarkerSign = gameBoard.getMarker().getMarkerSign();
        for(i =0 ;i<boardSize;i++)          //////FOR MARKER CONTROL IN INIT
        {
            for(j=0;j<boardSize;j++)
                if  (board[i][j].getValue().equals(MarkerSign)) {
                     gameBoard.getMarker().setMarkerLocation(i + 1, j + 1);
                    break;
                }
        }
    }


}
