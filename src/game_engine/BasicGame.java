package game_engine;

import game_objects.*;
import game_validation.XmlNotValidException;
import jaxb.schema.generated.GameDescriptor;
import jaxb.schema.generated.Range;

import java.util.List;
import java.util.Random;
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



    public String  TotalGameTime()
    {
        long millis = System.currentTimeMillis() - StartTime;
         return String.format("%02d:%02d",
               TimeUnit.MILLISECONDS.toMinutes(millis),
               TimeUnit.MILLISECONDS.toSeconds(millis) -
               TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

    }

    @Override
    public void gameOver()
    {
        Player winner;
        if(isEndOfGame) {
            int rowPlayerScore = getRowPlayer().getScore();
            int ColPlayerScore = getColPlayer().getScore();
            if (rowPlayerScore > ColPlayerScore)
                winner = rowPlayer;
            else if (ColPlayerScore > rowPlayerScore)
                winner = colPlayer;
            else //tie
            {
                winner = null;
            }
        }
        else {
            if (currentPlayer.checkPlayerTurn(rowPlayer)) {
                //row player left
                winner = colPlayer;
            } else {
                //col player left
                winner = rowPlayer;
            }
        }
        //print to UI winner
    }

   @Override
   public void initGame()
       {
           setBasicPlayers();
           setCurrentPlayer(rowPlayer);
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
//    }

    public boolean makeHumanMove(Point userPoint) //GET POINT FROM UI
    {
        boolean IsValidMove;
        int squareValue;
        squareValue = updateBoard(userPoint); //update 2 squares
        if (squareValue == BAD_SQUARE)
            IsValidMove = false;
        else {
            updateUserData(squareValue);
            gameBoard.getMarker().setMarkerLocation(userPoint.getRow() +1, userPoint.getCol() +1);
            IsValidMove = true;
        }
        return IsValidMove;
    }


    @Override
    public void switchPlayer()
    {
        if (currentPlayer.checkPlayerTurn(rowPlayer)) {
            if (gameBoard.isColPlayerHaveMoves(gameBoard.getMarker().getMarkerLocation()))
            {
                currentPlayer = colPlayer;
            }
            else {
                isEndOfGame = true;
               // UserInterface.PrintUserMessage("Col player have no moves ! GAME OVER");
                gameOver();
            }
        }
        else //(currentPlayer.equals(colPlayer))
        {
            if (gameBoard.isRowPlayerHaveMoves(gameBoard.getMarker().getMarkerLocation()))
                currentPlayer = rowPlayer;
            else {
                isEndOfGame = true;
               // UserInterface.PrintUserMessage("Row player have no moves ! GAME OVER");
                gameOver();
            }
        }
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
        int squareValue = 100;
        Point oldMarkerPoint = gameBoard.getMarker().getMarkerLocation();

        String squareStringValue = gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()].getValue();//get number
        if (squareStringValue.equals(gameBoard.getMarker().getMarkerSign()) || squareStringValue.isEmpty()) { //checks if wrong square-marker or empty
            return squareValue;
        }
        squareValue = Square.ConvertFromStringToIntValue(squareStringValue); //return number value

        gameBoard.getGameBoard()[oldMarkerPoint.getRow()-1][oldMarkerPoint.getCol()-1].setValue("");    //empty old marker location

        gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()].setValue( gameBoard.getMarker().markerSign); //update marker to square

        return squareValue;
    }

    /*@Override
    public int updateBoard(Point squareLocation) //implement in Board - returns updated value of row/column
    {
        int squareValue = gameBoard.updateBoard(squareLocation);
        return squareValue;
    }*/


    @Override
    public void updateUserData(int squareValue) // in Player?
    {
        currentPlayer.setNumOfMoves(currentPlayer.getNumOfMoves()+1); //maybe do totalmoves var in gameManager
        currentPlayer.setScore(squareValue);
    }

//    @Override
//    public void setBoard(jaxb.schema.generated.Board board)
//    {
//        eBoardType boardType = eBoardType.valueOf(board.getStructure().getType());
//        gameBoard = new Board(board.getSize().intValue(),boardType);
//
//        switch (boardType) {
//            case Explicit: Point markerLocation = new Point(board.getStructure().getSquares().getMarker().getRow().intValue(),board.getStructure().getSquares().getMarker().getColumn().intValue());
//                gameBoard.FillExplicitBoard(explicitSquares,markerLocation);
//                break;
//            case Random:
//                BoardRange range = new BoardRange(board.getStructure().getRange().getFrom(),board.getStructure().getRange().getTo());
//                gameBoard.setBoardRange(range);
//                gameBoard.FillRandomBoard();
//                break;
//        }
//    }

    public void setPlayers(int playerChoice)
    {

        switch (playerChoice)
        {
            case HUMAN_PLAYER:
                rowPlayer = new Player(eTurn.ROW, ePlayerType.Human);
                colPlayer = new Player(eTurn.COL, ePlayerType.Human);
                break;
            case COMPUTER_PLAYER:
                rowPlayer = new Player(eTurn.ROW, ePlayerType.Human);
                colPlayer = new Player(eTurn.COL, ePlayerType.Computer);
                break;
            case COMPUTERS_GAME:
                rowPlayer = new Player(eTurn.ROW, ePlayerType.Computer);
                colPlayer = new Player(eTurn.COL, ePlayerType.Computer);
                break;
        }
    }


    @Override
    public void checkXMLData(GameDescriptor loadedGame)throws XmlNotValidException
    {
        super.checkBoardXML(loadedGame.getBoard());
    }



    @Override
    public void checkRandomBoardValidity(Range boardRange, int boardSize)
    {

//        int range;
//
//        if(!(boardRange.getFrom() >= -99 &&  boardRange.getTo() <= 99))
//        {
//            //UserInterface.ValidationErrors.add(String.format("Random Board Validation Error: board range have to be in [-99,99] range,range [%d,%d] is invalid ",boardRange.getFrom(),boardRange.getTo()));
//            return isValidBoard;
//        }
//        if(boardRange.getFrom() <= boardRange.getTo())
//        {
//            range = boardRange.getTo() - boardRange.getFrom() +1;
//
//            if(((boardSize*boardSize -1) / range) > 0)
//            {
//                isValidBoard = true;
//            }
//            else
//            {
//                //UserInterface.ValidationErrors.add(String.format("Random Board Validation Error: Board Size %d < Board Range numbers %d (from %d to %d)",
//                    //    boardSize*boardSize,range,boardRange.getFrom(),boardRange.getTo()));
//            }
//        }
//        else
//        {
//          //  UserInterface.ValidationErrors.add(String.format("Random Board Validation Error: Board Range numbers invalid from %d > to %d",
//            //        boardRange.getFrom(),boardRange.getTo()));
//        }

    }

    @Override
    public void checkExplicitBoard(List<jaxb.schema.generated.Square> squares, jaxb.schema.generated.Marker marker,int boardSize)
    {

//        game_objects.Square newSquare;
//        int row,col,val,color;
//
//        if(!isInBoardRange(marker.getRow().intValue(),boardSize))
//        {
//            isValidBoard = false;
//          //  UserInterface.ValidationErrors.add(String.format("Explicit Board validation : invalid row of marker location ! Must be in range  from %d  to %d",1,boardSize));
//        }
//        if(!isInBoardRange(marker.getColumn().intValue(),boardSize))
//        {
//            isValidBoard = false;
//           // UserInterface.ValidationErrors.add(String.format("Explicit Board validation error: invalid column of marker location ! Must be in range  from %d  to %d",1,boardSize));
//        }
//
//        if(isValidBoard) {
//
//            for (jaxb.schema.generated.Square square : squares) {
//                row = square.getRow().intValue();
//                col = square.getColumn().intValue();
//                val = square.getValue().intValue();
//                color = square.getColor();
//
//                if ((val < -99 )|| (val > 99)) {
//                 //   UserInterface.ValidationErrors.add("Explicit Board Validation Error: squares values must be in between -99 and 99" );
//                    isValidBoard = false;
//                    break;
//
//                }
//                if(!(row == marker.getRow().intValue() && col == marker.getColumn().intValue())) {
//
//                    if (isInBoardRange(row, boardSize) && isInBoardRange(col, boardSize)) //location is ok
//                    {
//                        newSquare = new game_objects.Square(new Point(row, col), String.valueOf(val), color);
//                        if (explicitSquares.contains(newSquare)) {
//                            isValidBoard = false;
//                         //   UserInterface.ValidationErrors.add(String.format("Explicit Board validation error: square double location [%d,%d] existance!",
//                           //         square.getRow().intValue(), square.getColumn().intValue()));
//                            break;
//                        } else {
//                            explicitSquares.add(newSquare);
//                        }
//                    } else {
//                        explicitSquares.clear();
//                        isValidBoard = false;
//                       // UserInterface.ValidationErrors.add(String.format("Explicit Board Validation Error: Square row :%d,column:%d outside board size :%d", row, col, boardSize));
//                        break;
//                    }
//                }else{
//                    isValidBoard = false;
//                   // UserInterface.ValidationErrors.add(String.format("Explicit Board Validation Error: Square row :%d,column:%d is both @ marker location and play square location!", row, col));
//                    break;
//                }
//            }
//        }
    }

    @Override
    public void FillRandomBoard() {

        int i = 0;
        int j = 0;
        int boardSize = gameBoard.GetBoardSize();
        BoardRange boardRange = gameBoard.getBoardRange();
        Square[][] board = gameBoard.getGameBoard();
        Random rand = new Random();

        // filling our numbers in given range
        int rangeSize = boardRange.RangeSize();
        int printNumCount = (boardSize * boardSize -1) / rangeSize; //49/9=5
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



