package game_objects;

import game_engine.GameManager;

import java.util.ArrayList;
import java.util.List;

public class GameMove {

    private Player currentPlayer;
    private List<Player> players;
    private int moveNum;
    private Square chosenMove;
    private Board gameBoard;


    public GameMove(Board gameBoard, Player player, List<Player> gamePlayers, int moveNum, Square chosenMove){
        this.gameBoard = new Board(gameBoard);
        this.players = new ArrayList<>();
        this.chosenMove = new Square(chosenMove);
        this.players.addAll(gamePlayers);
        setCurrentPlayer(player);
        this.moveNum = moveNum;
    }


    public Board getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(Board gameBoard) {
        this.gameBoard = gameBoard;
    }

    private void setCurrentPlayer(Player player){
        currentPlayer = new Player(ePlayerType.valueOf(player.getPlayerType()),player.getName(),player.getId(),player.getColor());
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
