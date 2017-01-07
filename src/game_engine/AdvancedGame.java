package game_engine;

import game_objects.*;
import jaxb.schema.generated.*;
import jaxb.schema.generated.Board;
import jaxb.schema.generated.Marker;
import jaxb.schema.generated.Player;
import jaxb.schema.generated.Square;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


public class AdvancedGame extends GameLogic{

    @Override
    public void makeMove()
    {

    }

    @Override
    public void gameOver()
    {
       // Collections.sort(players, (p1, p2) -> p1.getScore - p2.getScore());
            //players in now sorted by SCORE
            //need to print to UI the players SCORES

    }

    @Override
    public boolean checkRandomBoardValidity(Range boardRange, int boardSize)
    {


        return true;
    }

    @Override
    public  boolean checkExplicitBoard(List<Square> squares, jaxb.schema.generated.Marker marker, int boardSize)
    {


        return true;
    }

    @Override
    public boolean InitMoveCheck()
    {
        return true;
    }

    @Override
    public Map<Integer, game_objects.Player> getPlayers() {return super.players;}

    @Override
    public game_objects.Player getCurrentPlayer() {return super.getCurrentPlayer();}

    @Override
    public void setCurrentPlayer(game_objects.Player currentPlayer) {super.setCurrentPlayer(currentPlayer);}

    @Override
    public int getNumOfPlayers() {return super.getNumOfPlayers();}

    @Override
    public void setNumOfPlayers(int num) {
        super.setNumOfPlayers(num);
    }

    @Override
    public void setBoard(Board board) {
      //  super.setBoard(board);
    }


    @Override
    protected int updateBoard(Point squareLocation) {
            int squareValue = gameBoard.updateBoard(squareLocation);
            return squareValue;

    }

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
        while (nextPlayer.isActive() == false)
        {
            nextPlayer = players.get((activePlayerNumber+1)%gameBoard.GetBoardSize());
        }
        while (!(isPlayerHaveMove(gameBoard.getMarker().getMarkerLocation(),nextPlayer)))
        {
            //need to update UI player have no move!!!
            nextPlayer = players.get((activePlayerNumber+1)%gameBoard.GetBoardSize());
            while (nextPlayer.isActive() == false)
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

    @Override
    public  boolean checkXMLData(GameDescriptor loadedGame)
    {
        boolean isValidXMLData;

        isValidXMLData = super.checkBoardXML(loadedGame.getBoard());
        if(isValidXMLData)
        {
            isValidXMLData = checkAndSetPlayersXML(loadedGame.getPlayers());
        }

        return isValidXMLData;
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

    private boolean makeHumanMove(Point userPoint)
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

    @Override
    public boolean checkAndSetPlayersXML(jaxb.schema.generated.Players players)
    {
        boolean areValidPlayers = true;
        List<Player> gamePlayers = players.getPlayer();
        game_objects.Player newPlayer ;

        if(gamePlayers.size() <MIN_PLAYERS || gamePlayers.size() > MAX_PLAYERS)
        {
            areValidPlayers = false;
            // UserInterface.ValidationErrors.add(String.format("Players Validation Error : %d - invalid numbers of players ," +
            //   "number of players must be minimun 2 and maximum 6 !",gamePlayers.size()));
        }
        else
        {
            setNumOfPlayers(gamePlayers.size());
            for(jaxb.schema.generated.Player player :gamePlayers)
            {
                newPlayer = new game_objects.Player(ePlayerType.valueOf(player.getType()),player.getName(),player.getId().intValue(),player.getColor());
                if(getPlayers().containsKey(newPlayer.getId()))
                {
                    areValidPlayers =false;
                    //  UserInterface.ValidationErrors.add(String.format("Player Validation Error: name = %s ,id = %d, color = %s already exists !",player.getName(),player.getId(),player.getColor()));
                    getPlayers().clear();
                    break;
                }
                else
                {
                    getPlayers().put(newPlayer.getId(),newPlayer);
                }
            }
        }

        return areValidPlayers;
    }
}
