package game_engine;

import game_objects.*;
import game_objects.Board;
import game_objects.Player;
import game_objects.Square;
import jaxb.schema.generated.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static game_engine.GameManager.BAD_SQUARE;

public class BasicGame extends GameLogic {

    private long StartTime;
    private Player rowPlayer;
    private Player colPlayer;
    private List<Square> explicitSquares = new ArrayList<Square>();

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
//        if(isEndOfGame) {
//            int rowPlayerScore = getRowPlayer().getScore();
//            int ColPlayerScore = getColPlayer().getScore();
//            if (rowPlayerScore > ColPlayerScore)
//                //UserInterface.PrintWinner("Row Player"); //rowPlayer.getName()
//            else if (ColPlayerScore > rowPlayerScore)
//               // UserInterface.PrintWinner("Column Player"); //colPlayer.getName()
//            else //(ColPlayerScore  == rowPlayerScore )
//               // UserInterface.PrintWinner("TIE");
//        }
//        else{
//            if(currentPlayer.checkPlayerTurn(rowPlayer))
//            {
//               // UserInterface.PrintUserMessage("Row player left the game ...");
//               // UserInterface.PrintWinner("Column Player");
//            }
//            else
//            {
//               // UserInterface.PrintUserMessage("Column player left the game ...");
//                //UserInterface.PrintWinner("Row Player");
//            }
//        }
       // UserInterface.PrintBoard(getGameBoard().toString());
        //UserInterface.ShowStatistics(getRowPlayer().getNumOfMoves()+getColPlayer().getNumOfMoves(),TotalGameTime(),getRowPlayer().getScore(),getColPlayer().getScore());
    }

    public static int ComputerMove(int boardSize) {
        return (ThreadLocalRandom.current().nextInt(1, boardSize + 1));
    }

    @Override
    public Map<Integer, Player> getPlayers()
    {
        return players;
    }


    public int makeComputerMove()
    {
        return ComputerMove(gameBoard.GetBoardSize());
    }

    @Override
    public void makeMove()
    {
        int chosenSquare;
        int squareValue = BAD_SQUARE ;
        Point squareLocation = null;
        boolean badInput = true;

        while (badInput) {
            switch (super.currentPlayer.getTurn()) {
                case ROW:
                    if (rowPlayer.getPlayerType() == ePlayerType.HUMAN) {
                       // chosenSquare = UserInterface.GetUserMove(gameBoard.getMarker().getMarkerLocation(), eTurn.ROW, gameBoard.GetBoardSize(), gameBoard.toString());
                       // squareLocation = new Point(gameBoard.getMarker().getMarkerLocation().getRow(), chosenSquare);
                        break;
                    }
                    else // COMPUTER
                    {
                        chosenSquare = makeComputerMove();
                        squareLocation = new Point(gameBoard.getMarker().getMarkerLocation().getRow(), chosenSquare);
                        break;
                    }
                case COL:
                    if (colPlayer.getPlayerType() == ePlayerType.HUMAN) {
                       // chosenSquare = UserInterface.GetUserMove(gameBoard.getMarker().getMarkerLocation(), eTurn.COL, gameBoard.GetBoardSize(), gameBoard.toString());
                       // squareLocation = new Point(chosenSquare, gameBoard.getMarker().getMarkerLocation().getCol());
                        break;
                    }
                    else // COMPUTER
                    {
                        chosenSquare = makeComputerMove();
                        squareLocation = new Point(chosenSquare, gameBoard.getMarker().getMarkerLocation().getCol());
                        break;
                    }
            }

            squareValue = super.updateBoard(squareLocation); //update 2 squares
            if (squareValue != BAD_SQUARE)
                badInput = false;
        //    else
         //   if (currentPlayer.getPlayerType() == ePlayerType.HUMAN)
               // UserInterface.PrintUserMessage("You choose invalid square! you can't select empty squares/marker square.choose another one..!");

        }
        if (currentPlayer.getPlayerType() == ePlayerType.COMPUTER)
        {
            //UserInterface.PrintUserMessage("computer " + currentPlayer.getTurn()+ " play his turn...he chose square ("+ squareLocation.getRow() + "," +squareLocation.getCol()+ ")");
        }
        updateUserData(squareValue); //update score and moves
        gameBoard.getMarker().setMarkerLocation(squareLocation.getRow(),squareLocation.getCol());
        //UserInterface.PrintBoard(gameBoard.toString());
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


    @Override
    public int updateBoard(Point squareLocation) //implement in Board - returns updated value of row/column
    {
        int squareValue = gameBoard.updateBoard(squareLocation);
        return squareValue;
    }


    @Override
    public void updateUserData(int squareValue) // in Player?
    {
        currentPlayer.setNumOfMoves(currentPlayer.getNumOfMoves()+1); //maybe do totalmoves var in gameManager
        currentPlayer.setScore(squareValue);
    }

    @Override
    public void setBoard(jaxb.schema.generated.Board board)
    {

        eBoardType boardType = eBoardType.valueOf(board.getStructure().getType());
        gameBoard = new Board(board.getSize().intValue(),boardType);

        switch (boardType) {
            case Explicit: Point markerLocation = new Point(board.getStructure().getSquares().getMarker().getRow().intValue(),board.getStructure().getSquares().getMarker().getColumn().intValue());
                gameBoard.FillExplicitBoard(explicitSquares,markerLocation);
                break;
            case Random:
                BoardRange range = new BoardRange(board.getStructure().getRange().getFrom(),board.getStructure().getRange().getTo());
                gameBoard.setBoardRange(range);
                gameBoard.FillRandomBoard();
                break;
        }
    }

    public void setPlayers(int playerChoice)
    {

        switch (playerChoice)
        {
            case HUMAN_PLAYER:
                rowPlayer = new Player(eTurn.ROW, ePlayerType.HUMAN);
                colPlayer = new Player(eTurn.COL, ePlayerType.HUMAN);
                break;
            case COMPUTER_PLAYER:
                rowPlayer = new Player(eTurn.ROW, ePlayerType.HUMAN);
                colPlayer = new Player(eTurn.COL, ePlayerType.COMPUTER);
                break;
            case COMPUTERS_GAME:
                rowPlayer = new Player(eTurn.ROW, ePlayerType.COMPUTER);
                colPlayer = new Player(eTurn.COL, ePlayerType.COMPUTER);
                break;
        }
    }


    @Override
    public boolean checkXMLData(GameDescriptor loadedGame)
    {
        int index;
        boolean isValidXMLData = false;

        String gameType = loadedGame.getGameType();
        isValidXMLData = checkBoardXML(loadedGame);

        if(isValidXMLData && (eGameType.valueOf(gameType) != eGameType.Basic) ) {
            if (eGameType.valueOf(gameType) == eGameType.Advance) {
                isValidXMLData = super.checkAndSetPlayersXML();
            }
            else if(eGameType.valueOf(gameType) == eGameType.AdvanceDynamic) {
                // isValidXMLData = checkAdvanceDynamicXML(loadedGame.getDynamicPlayers());
            }
            else {
                isValidXMLData = false;
              //  UserInterface.ValidationErrors.add(String.format("Game Type Error : No such game type %s !", gameType));
            }
        }
        return isValidXMLData;
    }



    @Override
    public boolean checkRandomBoardValidity(Range boardRange, int boardSize)
    {
        boolean isValidBoard = false;
        int range;

        if(!(boardRange.getFrom() >= -99 &&  boardRange.getTo() <= 99))
        {
            //UserInterface.ValidationErrors.add(String.format("Random Board Validation Error: board range have to be in [-99,99] range,range [%d,%d] is invalid ",boardRange.getFrom(),boardRange.getTo()));
            return isValidBoard;
        }
        if(boardRange.getFrom() <= boardRange.getTo())
        {
            range = boardRange.getTo() - boardRange.getFrom() +1;

            if(((boardSize*boardSize -1) / range) > 0)
            {
                isValidBoard = true;
            }
            else
            {
                //UserInterface.ValidationErrors.add(String.format("Random Board Validation Error: Board Size %d < Board Range numbers %d (from %d to %d)",
                    //    boardSize*boardSize,range,boardRange.getFrom(),boardRange.getTo()));
            }

        }
        else
        {
          //  UserInterface.ValidationErrors.add(String.format("Random Board Validation Error: Board Range numbers invalid from %d > to %d",
            //        boardRange.getFrom(),boardRange.getTo()));
        }

        return isValidBoard;

    }

    @Override
    public boolean checkExplicitBoard(Range range, List<jaxb.schema.generated.Square> squares, jaxb.schema.generated.Marker marker,int boardSize)
    {
        boolean isValidBoard = true;
        game_objects.Square newSquare;
        int row,col,val,color;

        if(!isInBoardRange(marker.getRow().intValue(),boardSize))
        {
            isValidBoard = false;
          //  UserInterface.ValidationErrors.add(String.format("Explicit Board validation : invalid row of marker location ! Must be in range  from %d  to %d",1,boardSize));
        }
        if(!isInBoardRange(marker.getColumn().intValue(),boardSize))
        {
            isValidBoard = false;
           // UserInterface.ValidationErrors.add(String.format("Explicit Board validation error: invalid column of marker location ! Must be in range  from %d  to %d",1,boardSize));
        }

        if(isValidBoard) {

            for (jaxb.schema.generated.Square square : squares) {
                row = square.getRow().intValue();
                col = square.getColumn().intValue();
                val = square.getValue().intValue();
                color = square.getColor();

                if ((val < -99 )|| (val > 99)) {
                 //   UserInterface.ValidationErrors.add("Explicit Board Validation Error: squares values must be in between -99 and 99" );
                    isValidBoard = false;
                    break;

                }
                if(!(row == marker.getRow().intValue() && col == marker.getColumn().intValue())) {

                    if (isInBoardRange(row, boardSize) && isInBoardRange(col, boardSize)) //location is ok
                    {
                        newSquare = new game_objects.Square(new Point(row, col), String.valueOf(val), color);
                        if (explicitSquares.contains(newSquare)) {
                            isValidBoard = false;
                         //   UserInterface.ValidationErrors.add(String.format("Explicit Board validation error: square double location [%d,%d] existance!",
                           //         square.getRow().intValue(), square.getColumn().intValue()));
                            break;
                        } else {
                            explicitSquares.add(newSquare);
                        }
                    } else {
                        explicitSquares.clear();
                        isValidBoard = false;
                       // UserInterface.ValidationErrors.add(String.format("Explicit Board Validation Error: Square row :%d,column:%d outside board size :%d", row, col, boardSize));
                        break;
                    }
                }else{
                    isValidBoard = false;
                   // UserInterface.ValidationErrors.add(String.format("Explicit Board Validation Error: Square row :%d,column:%d is both @ marker location and play square location!", row, col));
                    break;
                }
            }
        }

        return isValidBoard;
    }


    @Override
    public boolean isInBoardRange(int num, int size)
    {
        boolean isValid = true;
        if(num < 0 || num > size)
        {
            isValid = false;
        }
        return isValid;

    }

//    public void loadDataFromJaxbToGame(GameDescriptor loadedGame)
//    {
//        jaxb.schema.generated.Board loadedBoard = loadedGame.getBoard();
//        setBoard(loadedBoard);
//
//    }

//    public GameDescriptor loadGameFromFile(String fileName)
//    {
//        try {
//            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//            InputStream xmlFileInputStream = BasicGame.class.getResourceAsStream("/xml_resources/Numberiada.xsd");
//            Source schemaSource = new StreamSource(xmlFileInputStream);
//            Schema schema = schemaFactory.newSchema(schemaSource);
//            JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);
//
//            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//            unmarshaller.setSchema(schema);
//            GameDescriptor JaxbGame;
//            JaxbGame = (GameDescriptor) unmarshaller.unmarshal(new File(fileName));
//            return JaxbGame;
//        }
//        catch (JAXBException e) {
//            //Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, e);
//           // UserInterface.ValidationErrors.add("Load Game from File Error : Invalid XML !");
//            return null;
//        }
//        catch (SAXException e) {
//            // Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, e);
//          //  UserInterface.ValidationErrors.add( "Load Game from File Error : Invalid XML !");
//            return null;
//        }
//    }





}



