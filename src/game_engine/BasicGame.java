package game_engine;

import game_objects.*;
import game_validation.ValidationResult;
import game_validation.XmlNotValidException;
import jaxb.schema.generated.GameDescriptor;
import jaxb.schema.generated.Range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class BasicGame extends GameLogic {

    private long StartTime;
    private Player rowPlayer;
    private Player colPlayer;


    public void setStartTime(long startTime) {
        StartTime = startTime;
    }
    public Player getRowPlayer() {
        return rowPlayer;
    }
    public void setRowPlayer(Player rowPlayer) {
        this.rowPlayer = rowPlayer;
    }
    public Player getColPlayer() {
        return colPlayer;
    }


    @Override
    public Point makeComputerMove(){
        return new Point(0,0);
    }

    @Override
    public void initGame()
    {
        super.initGame();
        setBasicPlayers();
        setCurrentPlayer(rowPlayer);
    }

    public String TotalGameTime()
    {
        long millis = System.currentTimeMillis() - StartTime;
         return String.format("%02d:%02d",
               TimeUnit.MILLISECONDS.toMinutes(millis),
               TimeUnit.MILLISECONDS.toSeconds(millis) -
               TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

    }



    public String playerRetire()
    {
        String winnerPlayer =" ";
        if (currentPlayer.checkPlayerTurn(rowPlayer)) {
            //row player left
            winnerPlayer = "Column Player";
        } else {
            //col player left
            winnerPlayer = "Row Player";
        }
        players.clear();
        winners.clear();
        currentPlayer = null;
        setNumOfPlayers(0);
        gameMoves.set(0);
        return winnerPlayer;
    }


    public void setBasicPlayers()
    {
        players = new ArrayList<>();
        rowPlayer = new Player(eTurn.ROW, ePlayerType.Human);
        colPlayer = new Player(eTurn.COL, ePlayerType.Human);
        players.add(0,rowPlayer);
        players.add(1,colPlayer);
    }

    @Override
    public boolean isGameOver(){return true;}

    @Override
    public String gameOver()
    {
        int i=0;
        game_objects.Player player;
        String winnerStatistics="";
        Collections.sort(players);
        Collections.reverse(players);
        String winner1=" ";
        String winner2=" ";
        if (winners.size() == 1)
        {
            winner1 = winners.get(0).getName();
        }
        if (winners.size() == 2)
        {
            winner2 = winners.get(1).getName();
        }
        if (winner1 != rowPlayer.getName() && winner2 !=rowPlayer.getName() )
        {
            winnerStatistics += (String.format("player %s  with score %d\n",
                    rowPlayer.getName(),rowPlayer.getScore()));
        }
        if (winner1 != colPlayer.getName() && winner2 != colPlayer.getName() )
        {
            winnerStatistics += (String.format("player %s with score %d\n",
                    colPlayer.getName(),colPlayer.getScore()));
        }

        players.clear();
        winners.clear();
        currentPlayer = null;
        setNumOfPlayers(0);
        gameMoves.set(0);
        return winnerStatistics;
    }

    @Override
    protected void updateUserData(int squareValue) // in Player?
    {
        int newScore = currentPlayer.getScore()+squareValue;
        currentPlayer.setNumOfMoves(currentPlayer.getNumOfMoves()+1); //maybe do totalmoves var in gameManager
        currentPlayer.scoreProperty().set(newScore);
        currentPlayer.scoreStringProperty().set(String.valueOf(newScore));
    }


    @Override
    public List<Player> getPlayers()
    {
        return players;
    }


    @Override
    public String getWinner(){
        return null;
    }


    @Override
    public void updateDataMove(Point userPoint)
    {
        int squareValue;
        squareValue = updateBoard(userPoint); //update 2 squares
        updateUserData(squareValue);
        gameBoard.getMarker().setMarkerLocation(userPoint.getRow()+1, userPoint.getCol()+1);
    }

    public int isValidPoint(Point squareLocation)
    {
        Point markerPoint = gameBoard.getMarker().getMarkerLocation();
        eTurn turn = getCurrentPlayer().getTurn();
        int returnPointStatus = GOOD_POINT;
        if (turn.equals(eTurn.ROW))
        {
            if (squareLocation.getRow() != (markerPoint.getRow()-1))
                returnPointStatus = NOT_IN_MARKER_ROW_BASIC;
        }
        else if (turn.equals(eTurn.COL))
        {
            if (squareLocation.getCol() != (markerPoint.getCol()-1))
                returnPointStatus = NOT_IN_MARKER_COL_BASIC;
        }
        if  (gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()]
                .getValue().equals((gameBoard.getMarker().getMarkerSign()) ))
        {
            returnPointStatus = MARKER_SQUARE_BASIC;
        }
        if  (gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()]
                .getValue().isEmpty())
        {
            returnPointStatus = EMPTY_SQUARE_BASIC;
        }

        return returnPointStatus;
    }




    @Override
    public boolean switchPlayer()
    {
        boolean doSwitch = true;
        setGameMoves(getGameMoves()+1);
        if (currentPlayer.checkPlayerTurn(rowPlayer)) {
            if (gameBoard.isColPlayerHaveMoves(gameBoard.getMarker().getMarkerLocation()))
            {
                currentPlayer = colPlayer;
            }
            else {
                isEndOfGame = true;
                // UserInterface.PrintUserMessage("Col player have no moves ! GAME OVER");
                //gameOver();*/
                doSwitch = false;
            }
        }
        else //(currentPlayer.equals(colPlayer))
        {
            if (gameBoard.isRowPlayerHaveMoves(gameBoard.getMarker().getMarkerLocation())) {
                currentPlayer = rowPlayer;
            }
            else {
                isEndOfGame = true;
                // UserInterface.PrintUserMessage("Row player have no moves ! GAME OVER");
                //gameOver();*/
                doSwitch = false;
            }
        }
        return doSwitch;
    }



    @Override
    public boolean InitMoveCheck()
    {
        boolean canMove = true;

        if (!(gameBoard.isRowPlayerHaveMoves(gameBoard.getMarker().getMarkerLocation())))
        {
            isEndOfGame = true;
            //gameOver();
            canMove = false;
        }
        return canMove;
    }

    @Override
    public void checkXMLData(GameDescriptor loadedGame)throws XmlNotValidException
    {
        super.checkBoardXML(loadedGame.getBoard());
    }



    @Override
    public void checkRandomBoardValidity(Range boardRange, int boardSize)throws XmlNotValidException
    {
        int range;
        validationResult = new ValidationResult();

        if(!(boardRange.getFrom() >= BoardRange.MIN_BOARD_RANGE &&  boardRange.getTo() <= BoardRange.MAX_BOARD_RANGE))
        {
            validationResult.add(String.format("Random Board Validation Error: board range have to be in [-99,99] range,range [%d,%d] is invalid ",
                    boardRange.getFrom(),boardRange.getTo()));
            throw new XmlNotValidException(validationResult);
        }
        if(boardRange.getFrom() <= boardRange.getTo())
        {
            range = boardRange.getTo() - boardRange.getFrom() +1;

            if(((boardSize*boardSize -1) / range == 0))
            {
                validationResult.add(String.format("Random Board Validation Error: Board Size %d < Board Range %d!",
                        boardSize*boardSize,range));
                throw new XmlNotValidException(validationResult);
            }

        } else {
            validationResult.add(String.format("Random Board Validation Error: Board Range numbers invalid from %d > to %d",
                    boardRange.getFrom(),boardRange.getTo()));
            throw new XmlNotValidException(validationResult);
        }


    }

    @Override
    public void FillRandomBoard() {

        int i = 0;
        int j = 0;
        int boardSize = gameBoard.GetBoardSize();
        BoardRange boardRange = gameBoard.getBoardRange();
        Square[][] board = gameBoard.getGameBoard();

        // filling our numbers in given range
        int rangeSize = boardRange.RangeSize();
        int printNumCount = (boardSize * boardSize -1) / rangeSize;
        int rangeNumToPrint = boardRange.getFrom();


        for(int m = 0;m < rangeSize && i< boardSize;m++) {
            for (int k = 0; k < printNumCount && i< boardSize; k++) {

                board[i][j].setValue(Square.ConvertFromIntToStringValue(rangeNumToPrint));
                board[i][j].setColor(GameColor.GRAY);
                j++;
                if (j == boardSize) {
                    i++;
                    j = 0;
                }
            }
            rangeNumToPrint++;
        }

        if (j == boardSize) {
            j = 0;
        }

        for (int m = i; m < boardSize; m++) {
            for (int n = j; n < boardSize; n++) {
                board[m][n].setValue("");
                board[i][j].setColor(GameColor.GRAY);
            }
        }

        board[boardSize - 1][boardSize - 1].setValue(Marker.markerSign);
        gameBoard.shuffleArray(board);

        String MarkerSign = gameBoard.getMarker().getMarkerSign();
        for(i =0 ;i<boardSize;i++)          //////FOR MARKER CONTROL IN INIT
        {
            for(j=0;j<boardSize;j++)
                if  (board[i][j].getValue().equals( MarkerSign)) {
                    gameBoard.getMarker().setMarkerLocation(i + 1, j + 1);
                    break;
                }
        }
    }


}



