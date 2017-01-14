package user_interface;

import game_objects.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.util.List;

public class NumberiadaBuilder {


    public static final int squareSize = 40;

    private GridPane board;
    private GridPane m_players;
    private List <Player> players;
    private Stage gameWindow;
    public Point chosenPoint;

    public Point getChosenPoint() {
        return chosenPoint;
    }

    public void setChosenPoint(Point chosenPoint) {
        this.chosenPoint = chosenPoint;
    }

    public GridPane getPlayersTable()
    {
        return m_players;
    }
    public void setGameWindow(Stage stage) {
        gameWindow = stage;
    }
    public Stage getGameWindow(){return gameWindow;}

    public void createPlayersTable(List<Player> gamePlayers)
    {
        players = gamePlayers;
        m_players = new GridPane();
        m_players.setPadding(new Insets(10, 10, 10, 40));
        m_players.setVgap(8);
        m_players.setHgap(10);

        Label name = new Label("Player Name");
        name.setId("player-gridPane");
        name.getStyleClass().add("player-gridPane");


        GridPane.setConstraints(name, 0, 0);
        Label id = new Label("ID");
        id.setId("player-gridPane");
        id.getStyleClass().add("player-gridPane");

        GridPane.setConstraints(id, 1, 0);

        Label type = new Label("Type");
        type.setId("player-gridPane");
        type.getStyleClass().add("player-gridPane");

        GridPane.setConstraints(type, 2, 0);

        Label color = new Label("Color");
        color.setId("player-gridPane");
        color.getStyleClass().add("player-gridPane");

        GridPane.setConstraints(color, 3,0);
        m_players.getChildren().addAll(name,id,type,color);
        int i=1;

        for (Player player : players)
        {
            Label nameP = new Label(player.getName());
            GridPane.setConstraints(nameP, 0, i);
            Label idP = new Label((String.valueOf(player.getId())));
            GridPane.setConstraints(idP, 1, i);
            Label typeP = new Label(player.getPlayerType());
            GridPane.setConstraints(typeP, 2, i);
            Label colorP = new Label(GameColor.getColor(player.getColor()));
            GridPane.setConstraints(colorP, 3, i);
            m_players.getChildren().addAll(nameP,idP,typeP,colorP);
            i++;
        }
    }


    public GridPane createBoard(Board gameBoard) {

        int size = gameBoard.GetBoardSize();
        Square[][]gBoard = gameBoard.getGameBoard();
        board = new GridPane();
        board.setPadding(new Insets(30, 30, 30, 30));
        board.setVgap(1);
        board.setHgap(1);

        for(int i = 0; i <= size; i++) {
            ColumnConstraints column = new ColumnConstraints(squareSize);
            column.setMinWidth(squareSize);
            board.getColumnConstraints().add(column);

        }

        for(int i = 0; i <= size; i++) {
            RowConstraints row = new RowConstraints(squareSize);
            row.setMinHeight(squareSize);
            board.getRowConstraints().add(row);
        }

        int i,j;
        Label lab = new Label("");
        lab.setPrefSize(squareSize,squareSize);
        lab.setAlignment(Pos.CENTER);

        board.add(lab,0,0);

        for(i=1;i<=size;i++)
        {
            lab = new Label(Integer.toString(i));
            lab.setPrefSize(squareSize,squareSize);
            lab.setAlignment(Pos.CENTER);
            board.add(lab,i,0);
        }

        for (j =1;j <=size; j++) {
            for ( i=0 ; i <=size; i++) {
                if (i==0) {
                    lab = new Label(Integer.toString(j));
                    lab.setPrefSize(squareSize,squareSize);
                    lab.setAlignment(Pos.CENTER);
                    board.add(lab,i,j);
                }
                else
                {
                    BoardButton butt = new BoardButton(gBoard[j-1][i-1]);
                    gBoard[j-1][i-1].colorProperty().addListener((observable, oldValue, newValue) -> butt.setBColor((int) newValue));
                    butt.textProperty().bind(gBoard[j-1][i-1].squareValueProperty());
                    butt.setPrefSize(squareSize,squareSize);
                    butt.setAlignment(Pos.CENTER);
                    butt.setOnAction(e->PressedBoardButton(butt));
                    board.add(butt,i,j);

                }
            }
        }

        return board;
    }

    public void clearBoard()
    {
        board.getChildren().remove(0,board.getChildren().size()-1);

    }
    public void clearPlayersView()
    {
        m_players.getChildren().remove(0,m_players.getChildren().size()-1);
    }

//    private ObservableList<ObservableList<Square>> createObservableBoard(Board logicBoard)
//    {
//         Square[][] gameBoard = logicBoard.getGameBoard();
//         int size = logicBoard.GetBoardSize();
//
//        ObservableList<ObservableList<Square>> board = FXCollections.<ObservableList<Square>>observableArrayList();
//        for (int i = 0; i < size; i++) {
//            final ObservableList<Square> row = FXCollections.<Square>observableArrayList();
//            board.add(i, row);
//            for (int j = 0; j < size; j++) {
//                row.add(gameBoard[i][j]);
//            }
//        }
//        return board;
//    }


    public void setPlayersScore(GridPane PlayerScoreGridPane)
    {
        int i=1;
        PlayerScoreGridPane.setPadding(new Insets(5, 5, 5, 5));
        PlayerScoreGridPane.setVgap(8);
        PlayerScoreGridPane.setHgap(8);
        PlayerScoreGridPane.getChildren().get(0).setStyle("-fx-background-color:#efff11;"+"-fx-border-color: #cc0e1a");
        PlayerScoreGridPane.getChildren().get(1).setStyle("-fx-background-color:#efff11;"+"-fx-border-color: #cc0e1a");

        for (Player player : players)
        {
            Label name = new Label(player.getName());
            name.textProperty().bind(player.nameProperty());
            name.setId("player-score-label");
            name.getStyleClass().add("player-score-label");
            String scoree = String.valueOf(player.getScore());
            Label score = new Label();
            score.setText(scoree);
            score.setId("score-label");
            score.getStyleClass().add("score-label");
            score.textProperty().bind(player.scoreStringProperty());
            PlayerScoreGridPane.addRow(i, name, score);
            i++;
        }
    }

    public void clearPlayersScoreView(GridPane PlayerScoreGridPane){
        PlayerScoreGridPane.getChildren().remove(2,PlayerScoreGridPane.getChildren().size());
    }

    public void setCurrentMove(Label MoveNumberLabel,int move)
    {
        MoveNumberLabel.setText(String.valueOf(move));
    }
    private void PressedBoardButton(BoardButton butt)
    {
        setChosenPoint(butt.getLocation());
    }

}
