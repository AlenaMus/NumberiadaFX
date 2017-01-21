package game_objects;

import game_engine.GameManager;
import game_engine.eGameType;

import java.util.ArrayList;
import java.util.List;

public class GameMove {

    private Player currentPlayer;
    private List<Player> players;
    private int moveNum;
    private Square chosenMove;
    private Board gameBoard;


    public GameMove(eGameType type,Board gameBoard, Player player, List<Player> gamePlayers, int moveNum){
        this.gameBoard = new Board(gameBoard);
        this.players = new ArrayList<>();

        setCurrentGamePlayers(gamePlayers,type);
        setCurrentPlayer(player,type);
        this.moveNum = moveNum;
    }


    public void setChosenSquare(Square sq){
        this.chosenMove = new Square(chosenMove);
    }
    public Board getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(Board gameBoard) {
        this.gameBoard = gameBoard;
    }

    private void setCurrentPlayer(Player player,eGameType type){
        if(type.equals(eGameType.Advance))
        currentPlayer = new Player(ePlayerType.valueOf(player.getPlayerType()),player.getName(),player.getId(),player.getColor());
        else if(type.equals(eGameType.Basic)){
            currentPlayer = new Player(player.getTurn(),ePlayerType.valueOf(player.getPlayerType()),player.getScore());
        }
    }
    private void setCurrentGamePlayers(List<Player> players,eGameType type){

        if(type.equals(eGameType.Advance)){
            for (Player player: players) {
                this.players.add(new Player(player));
            }
        }else if(type.equals(eGameType.Basic)){
            for (Player player: players) {
                this.players.add(new Player(player.getTurn(),ePlayerType.valueOf(player.getPlayerType()),player.getScore()));
            }

        }


    }

    public void clear(){

        this.gameBoard.clearBoard();
        this.players.clear();
        chosenMove = null;
        currentPlayer = null;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getMoveNum() {
        return moveNum;
    }

    public void setMoveNum(int moveNum) {
        this.moveNum = moveNum;
    }

    public Square getChosenMove() {
        return chosenMove;
    }

    public void setChosenMove(Square chosenMove) {
        this.chosenMove = chosenMove;
    }
}
