package game_engine;

import game_objects.*;
import game_validation.ValidationResult;
import game_validation.XmlNotValidException;
import jaxb.schema.generated.GameDescriptor;
import jaxb.schema.generated.Player;
import jaxb.schema.generated.Range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class AdvancedGame extends GameLogic{

    public static final int MIN_PLAYERS = 3;
    public static final int MAX_PLAYERS = 6;
    private int currentPlayerIndex;


    @Override
    public void initGame()
    {
        super.setCurrentPlayer(players.get(0));
        currentPlayerIndex = 0;
    }


    public void setWinners(){
        int maxScore=-10000;
        for (game_objects.Player player:players) {
                maxScore = player.getScore();
                break;
        }
        for (game_objects.Player player:players) {
            if(player!=null)
            {
                if(player.getScore()> maxScore){
                    maxScore = player.getScore();
                }
            }
        }
        for (game_objects.Player player:players) {
            if(player!=null)
            {
                if(player.getScore()== maxScore){
                    winners.add(player);
                }
            }
        }
    }

    @Override
    public String getWinner(){
        String winnerMessage="";
        setWinners();
       if(winners.size()> 1){
           winnerMessage = "It's a TIE!\nThe Winners are :\n";
           for (game_objects.Player player:winners) {
               winnerMessage+=String.format("%s id:%d -> score :%d\n",player.getName(),player.getId(),player.getScore());
           }
       }else{
           for (game_objects.Player player:winners) {
               if(player!=null){
                   winnerMessage = "The Winner is:\n" +
                           String.format("%s id : %d -> score : %d", player.getName(), player.getId(), player.getScore());
                   break;
               }
           }
       }
        return winnerMessage;
    }

    @Override
    public String gameOver()
    {
        int i=0;
        game_objects.Player player;
        String winnerStatistics="";
        Collections.sort(players);
        Collections.reverse(players);
        while(i < players.size())
        {
            player = players.get(i);
            if(!winners.contains(player)){
                winnerStatistics += (String.format("player %s id: %d with score %d\n",
                        player.getName(),player.getId(),player.getScore()));
            }
            i++;
        }

        players.clear();
        winners.clear();
        currentPlayer = null;
        setNumOfPlayers(0);
        gameMoves.set(0);
        return winnerStatistics;
    }

    @Override
    public boolean InitMoveCheck()
    {
        return isPlayerHaveMove(gameBoard.getMarker().getMarkerLocation(),currentPlayer);
    }


    @Override
    public void makeMove()
    {

    }


    @Override
    public boolean switchPlayer()
    {
       boolean isSwitchSucceed = true;
       game_objects.Player nextPlayer;

            currentPlayerIndex++;
            nextPlayer = players.get(currentPlayerIndex % numOfPlayers);
            super.setCurrentPlayer(nextPlayer);

        while (!nextPlayer.isActive())
        {
            currentPlayerIndex++;
            nextPlayer = players.get(currentPlayerIndex % numOfPlayers);
        }
        if (!(isPlayerHaveMove(gameBoard.getMarker().getMarkerLocation(),nextPlayer)))
        {
             isSwitchSucceed = false;

        }else{
            setGameMoves(getGameMoves()+1);
        }

        return isSwitchSucceed;
    }

    private boolean isPlayerHaveMove(Point markerLocation, game_objects.Player player)
    {
        int MarkerRow = markerLocation.getRow()-1;
        int MarkerCol = markerLocation.getCol()-1;
        for (int i=0; i < gameBoard.GetBoardSize();i++)
            if ((!gameBoard.getGameBoard()[MarkerRow][i].isDisabled()) &&(!gameBoard.getGameBoard()[MarkerRow][i].isEmpty())
                    && (!gameBoard.getGameBoard()[MarkerRow][i].getValue().equals(gameBoard.getMarker().getMarkerSign()))
                    && ((gameBoard.getGameBoard()[MarkerRow][i].getColor()  == (player.getColor()))))
                return true;
        for (int i=0; i < gameBoard.GetBoardSize(); i++)
            if ((!gameBoard.getGameBoard()[i][MarkerCol].isDisabled() )&& (!gameBoard.getGameBoard()[i][MarkerCol].isEmpty() )
                    && (!gameBoard.getGameBoard()[i][MarkerCol].getValue().equals(gameBoard.getMarker().getMarkerSign()))
                    && ((gameBoard.getGameBoard()[i][MarkerCol].getColor()  == (player.getColor()))))
                return true;
        return false;
    }

    @Override
    public boolean isGameOver()
    {
        boolean gameOver = true;
        Point markerLocation = gameBoard.getMarker().getMarkerLocation();
        int MarkerRow = markerLocation.getRow()-1;
        int MarkerCol = markerLocation.getCol()-1;
        for (int i=0; i < gameBoard.GetBoardSize();i++)
            if ((!gameBoard.getGameBoard()[MarkerRow][i].isDisabled()) &&(!gameBoard.getGameBoard()[MarkerRow][i].isEmpty())
                    && (!gameBoard.getGameBoard()[MarkerRow][i].getValue().equals(gameBoard.getMarker().getMarkerSign())))
               gameOver = false;
        for (int i=0; i < gameBoard.GetBoardSize(); i++)
            if ((!gameBoard.getGameBoard()[i][MarkerCol].isDisabled() )&& (!gameBoard.getGameBoard()[i][MarkerCol].isEmpty() )
                    && (!gameBoard.getGameBoard()[i][MarkerCol].getValue().equals(gameBoard.getMarker().getMarkerSign())))
               gameOver = false;

        return gameOver;
    }

    public void playerRetire () {
        for (int i = 0; i < gameBoard.GetBoardSize(); i++)
            for (int j = 0; j < gameBoard.GetBoardSize(); j++)
                if (gameBoard.getGameBoard()[i][j].getColor() == currentPlayer.getColor())
                {
                    gameBoard.getGameBoard()[i][j].setColor(GameColor.GRAY);
                    gameBoard.getGameBoard()[i][j].setDisabled(true);
                    gameBoard.getGameBoard()[i][j].setValue(" ");
                    gameBoard.getGameBoard()[i][j].setEmpty(true);
                }
        currentPlayer.setActive(false);
        players.remove(currentPlayer);
        numOfPlayers--;
       if(numOfPlayers==1) {
           isEndOfGame = true;
       }
    }




    public static int ComputerMove(int boardSize) {
        return (ThreadLocalRandom.current().nextInt(0, boardSize));
    }

    public void makeComputerMove()
    {
        int squareValue ;
        boolean foundSquare = false;
        Point squareLocation = null;
        int MarkerRow = gameBoard.getMarker().getMarkerLocation().getRow()-1;
        int MarkerCol = gameBoard.getMarker().getMarkerLocation().getCol()-1;

        while (!foundSquare) {
            int random = ComputerMove(gameBoard.GetBoardSize());
            if (gameBoard.getGameBoard()[MarkerRow][random].getColor() == (currentPlayer.getColor())) {
                squareLocation = new Point(MarkerRow, random);
                foundSquare = true;
            }
            else if (gameBoard.getGameBoard()[random][MarkerCol].getColor() == (currentPlayer.getColor())) {
                squareLocation = new Point(random, MarkerCol);
                foundSquare = true;
            }
        }
        squareValue = updateBoard(squareLocation); //update 2 squares
        updateUserData(squareValue);
        gameBoard.getMarker().setMarkerLocation(squareLocation.getRow()+1,squareLocation.getCol()+1);

    }

    public void makeHumanMove(Point userPoint)
    {
        int squareValue;
        squareValue = updateBoard(userPoint); //update 2 squares
        updateUserData(squareValue);
        gameBoard.getMarker().setMarkerLocation(userPoint.getRow()+1, userPoint.getCol()+1);

    }

    public int isValidPoint(Point squareLocation)
    {
        /*   if (squareStringValue.equals(gameBoard.getMarker().getMarkerSign()) || squareStringValue.isEmpty()) { //checks if wrong square-marker or empty
            return squareValue;
        }*/
        int returnPointStatus = GOOD_POINT;
        Point markerPoint = gameBoard.getMarker().getMarkerLocation();
        if (squareLocation.getRow() != (markerPoint.getRow()-1) && squareLocation.getCol() != (markerPoint.getCol()-1)  )
            returnPointStatus = NOT_IN_MARKER_ROW_AND_COLUMN;
        else if  (currentPlayer.getColor() != gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()].getColor())
            returnPointStatus = NOT_PLAYER_COLOR;
        return returnPointStatus;
    }

    public int updateBoard(Point squareLocation)
    {
        int squareValue;

        Point oldMarkerPoint = gameBoard.getMarker().getMarkerLocation();
        String squareStringValue = gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()].getValue();//get number
        squareValue = game_objects.Square.ConvertFromStringToIntValue(squareStringValue); //return number value

        gameBoard.getGameBoard()[oldMarkerPoint.getRow()-1][oldMarkerPoint.getCol()-1].setValue("");    //empty old marker location
        //gameBoard.getGameBoard()[oldMarkerPoint.getRow()-1][oldMarkerPoint.getCol()-1].setColor(GameColor.GRAY);
        gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()].setValue(Marker.markerSign); //update marker to square
        gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()].setColor(GameColor.GRAY); //update marker to square

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
