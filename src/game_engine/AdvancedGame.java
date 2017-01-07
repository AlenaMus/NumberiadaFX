package game_engine;

import game_objects.*;
import jaxb.schema.generated.*;
import jaxb.schema.generated.Board;
import jaxb.schema.generated.Marker;
import jaxb.schema.generated.Player;
import jaxb.schema.generated.Square;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdvancedGame extends GameLogic{


    @Override
    public void setBoard(Board board) {
        super.setBoard(board);
    }


    @Override
    public void makeMove()
    {

    }

    @Override
    public void gameOver()
    {

    }

    @Override
    public boolean checkRandomBoardValidity(Range boardRange, int boardSize)
    {
        return true;
    }

    @Override
    public  boolean checkExplicitBoard(Range range, List<Square> squares, jaxb.schema.generated.Marker marker, int boardSize)
    {
        return true;
    }

    @Override
    public boolean InitMoveCheck()
    {
        return true;
    }

    @Override
    public Map<Integer, game_objects.Player> getPlayers()
    {
        return super.players;
    }


    @Override
    public game_objects.Player getCurrentPlayer() {
        return super.getCurrentPlayer();
    }

    @Override
    public void setCurrentPlayer(game_objects.Player currentPlayer) {
        super.setCurrentPlayer(currentPlayer);
    }

    @Override
    public int getNumOfPlayers() {
        return super.getNumOfPlayers();
    }

    @Override
    public void setNumOfPlayers(int num) {
        super.setNumOfPlayers(num);
    }

    @Override
    public void loadDataFromJaxbToGame(GameDescriptor loadedGame) {
        super.loadDataFromJaxbToGame(loadedGame);
    }

//    @Override
//    public boolean LoadGameFromXmlAndValidate() {
//        return true;
//    }

    @Override
    protected int updateBoard(Point squareLocation) {
        return super.updateBoard(squareLocation);
    }

    @Override
    protected void updateUserData(int squareValue) {
        super.updateUserData(squareValue);
    }

    @Override
    protected void switchPlayer() {
        super.switchPlayer();
    }

    @Override
    protected boolean checkBoardXML(GameDescriptor loadedGame) {
        return super.checkBoardXML(loadedGame);
    }

    @Override
    protected boolean checkAndSetPlayersXML()
    {
        return super.checkAndSetPlayersXML();
    }

    @Override
    public  boolean checkXMLData(GameDescriptor loadedGame)
    {
        return true;
    }

    @Override
    public boolean isInBoardRange(int num, int size)
    {
        return true;
    }

    private boolean checkAndSetPlayersXML(jaxb.schema.generated.Players players) //advanced game
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
