package game_engine;

import game_objects.*;
import game_validation.ValidationResult;
import game_validation.XmlNotValidException;
import jaxb.schema.generated.GameDescriptor;
import jaxb.schema.generated.Range;

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


    public void makeComputerMove()
    {}


    public void initGame()
    {
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

    @Override
    public String gameOver()
    {
        Player winner;
        String winnerPlayer =" ";

        if(isEndOfGame) {
            int rowPlayerScore = getRowPlayer().getScore();
            int ColPlayerScore = getColPlayer().getScore();
            if (rowPlayerScore > ColPlayerScore)
                winnerPlayer = rowPlayer.getName();
            else if (ColPlayerScore > rowPlayerScore)
                winnerPlayer = colPlayer.getName();
            else //tie
            {
                winnerPlayer =" ";
            }
        }
       /* else {
            if (currentPlayer.checkPlayerTurn(rowPlayer)) {
                //row player left
                winnerPlayer = colPlayer.getName();
            } else {
                //col player left
                winnerPlayer = rowPlayer.getName();
            }
        }*/

        return winnerPlayer;
    }

    public String playerRetire()
    {
        String winnerPlayer =" ";
        if (currentPlayer.checkPlayerTurn(rowPlayer)) {
            //row player left
            winnerPlayer = colPlayer.getName();
        } else {
            //col player left
            winnerPlayer = rowPlayer.getName();
        }
        return winnerPlayer;
    }


    public void setBasicPlayers()
    {
        rowPlayer = new Player(eTurn.ROW, ePlayerType.Human);
        colPlayer = new Player(eTurn.COL, ePlayerType.Human);
    }

//    public static int ComputerMove(int boardSize) {
//        return (ThreadLocalRandom.current().nextInt(1, boardSize + 1));
//    }

    @Override
    public List<Player> getPlayers()
    {
        return players;
    }


    @Override
    public void makeMove()
    {

    }

    @Override
    public String getWinner(){
        return null;
    }

    @Override
    public boolean isGameOver(){return true;}
    //public int makeComputerMove()
//    {
//        return ComputerMove(gameBoard.GetBoardSize());
//    }

//    @Override
//    public void makeMove()
//    {
//        int chosenSquare;
//        int squareValue = BAD_SQUARE ;
//        Point squareLocation = null;
//        boolean badInput = true;
//
//        while (badInput) {
//            switch (super.currentPlayer.getTurn()) {
//                case ROW:
//                    if (rowPlayer.getPlayerType() == ePlayerType.Human) {
//                       // chosenSquare = UserInterface.GetUserMove(gameBoard.getMarker().getMarkerLocation(), eTurn.ROW, gameBoard.GetBoardSize(), gameBoard.toString());
//                       // squareLocation = new Point(gameBoard.getMarker().getMarkerLocation().getRow(), chosenSquare);
//                        break;
//                    }
//                    else // Computer
//                    {
//                        chosenSquare = makeComputerMove();
//                        squareLocation = new Point(gameBoard.getMarker().getMarkerLocation().getRow(), chosenSquare);
//                        break;
//                    }
//                case COL:
//                    if (colPlayer.getPlayerType() == ePlayerType.Human) {
//                       // chosenSquare = UserInterface.GetUserMove(gameBoard.getMarker().getMarkerLocation(), eTurn.COL, gameBoard.GetBoardSize(), gameBoard.toString());
//                       // squareLocation = new Point(chosenSquare, gameBoard.getMarker().getMarkerLocation().getCol());
//                        break;
//                    }
//                    else // Computer
//                    {
//                        chosenSquare = makeComputerMove();
//                        squareLocation = new Point(chosenSquare, gameBoard.getMarker().getMarkerLocation().getCol());
//                        break;
//                    }
//            }
//
//            squareValue = super.updateBoard(squareLocation); //update 2 squares
//            if (squareValue != BAD_SQUARE)
//                badInput = false;
//        //    else
//         //   if (currentPlayer.getPlayerType() == ePlayerType.Human)
//               // UserInterface.PrintUserMessage("You choose invalid square! you can't select empty squares/marker square.choose another one..!");
//
//        }
//        if (currentPlayer.getPlayerType() == ePlayerType.Computer)
//        {
//            //UserInterface.PrintUserMessage("computer " + currentPlayer.getTurn()+ " play his turn...he chose square ("+ squareLocation.getRow() + "," +squareLocation.getCol()+ ")");
//        }
//        updateUserData(squareValue); //update score and moves
//        gameBoard.getMarker().setMarkerLocation(squareLocation.getRow(),squareLocation.getCol());
//        //UserInterface.PrintBoard(gameBoard.toString());


    public void makeHumanMove(Point userPoint)
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
            if (gameBoard.isRowPlayerHaveMoves(gameBoard.getMarker().getMarkerLocation()))
                currentPlayer = rowPlayer;
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
            //UserInterface.PrintUserMessage("Row player have no moves ! GAME OVER");
            gameOver();
            canMove = false;
        }
        return canMove;
    }



    public int updateBoard(Point squareLocation)
    {
        int squareValue;

        Point oldMarkerPoint = gameBoard.getMarker().getMarkerLocation();
        String squareStringValue = gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()].getValue();//get number
        squareValue = game_objects.Square.ConvertFromStringToIntValue(squareStringValue); //return number value

        gameBoard.getGameBoard()[oldMarkerPoint.getRow()-1][oldMarkerPoint.getCol()-1].setValue("");    //empty old marker location
        gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()].setValue(Marker.markerSign); //update marker to square
        gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()].setColor(GameColor.GRAY);

        return squareValue;
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



