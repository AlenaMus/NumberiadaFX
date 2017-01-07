package user_interface;

import game_objects.GameColor;
import game_objects.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import game_objects.ePlayerType;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class NumberiadaBuilder {


    private static final int squareSize = 20;

    private GridPane board;
    private GridPane m_players;
    List<Player> players; //Get From Logics
    Player currentPlayer = new Player(ePlayerType.HUMAN,"Koko Chanel",5,5);
    int moveNum = 1;

    public void setPlayers() { //take players from Logic
        players = new ArrayList<>();
        players.add(new Player(ePlayerType.HUMAN,"Kate Hanks",20, 1));
        players.add(new Player(ePlayerType.HUMAN,"Dana Shir",6, 2));
        players.add(new Player(ePlayerType.COMPUTER,"Doroty",4, 3));
        players.add(new Player(ePlayerType.HUMAN,"Timon",0, 4));
        players.add(new Player(ePlayerType.COMPUTER,"Kara",56, 5));
        players.add(new Player(ePlayerType.COMPUTER,"Dan Veizman",78, 6));
    }

    public GridPane getPlayersTable()
    {
        return m_players;
    }

    public void createPlayersTable()//List<Player> players)
    {

        setPlayers();
        m_players = new GridPane();
        m_players.setPadding(new Insets(10, 10, 10, 40));
        m_players.setVgap(8);
        m_players.setHgap(10);
        Label name = new Label("Player Name");
        name.setStyle("-fx-font-weight: bold;" +
                "-fx-text-fill: #AB4642;");
        GridPane.setConstraints(name, 0, 0);
        Label id = new Label("ID");
        id.setStyle("-fx-font-weight: bold;" +
                "-fx-text-fill: #AB4642;");
        GridPane.setConstraints(id, 1, 0);
        Label type = new Label("Type");
        type.setStyle("-fx-font-weight: bold;" +
                "-fx-text-fill: #AB4642;");
        GridPane.setConstraints(type, 2, 0);
        Label color = new Label("Color");
        color.setStyle("-fx-font-weight: bold;" +
                "-fx-text-fill: #AB4642;");
        GridPane.setConstraints(color, 3,0);
        m_players.getChildren().addAll(name,id,type,color);
        int i=1;

        for (Player player:players) {

            Label nameP = new Label(player.getName());
            GridPane.setConstraints(nameP, 0, i);
            Label idP = new Label((String.valueOf(player.getId())));
            GridPane.setConstraints(idP, 1, i);
            Label typeP = new Label(player.getPlayerType().toString());
            GridPane.setConstraints(typeP, 2, i);
            Label colorP = new Label(GameColor.getColor(player.getColor()));
            GridPane.setConstraints(colorP, 3, i);
            m_players.getChildren().addAll(nameP,idP,typeP,colorP);
            i++;
        }



    }

    public GridPane createBoard(int size) {
        board = new GridPane();
        board.setPrefSize(400,400);
        board.setPadding(new Insets(30, 10, 10, 10));
        board.setVgap(1);
        board.setHgap(1);

        for(int i = 0; i < size; i++) {
            ColumnConstraints column = new ColumnConstraints(squareSize);
            board.getColumnConstraints().add(column);
        }

        for(int i = 0; i < size; i++) {
            RowConstraints row = new RowConstraints(squareSize);
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
                    Button butt = new Button("A");
                    butt.setPrefSize(squareSize,squareSize);
                    butt.getStyleClass().add("button-blue");
                    butt.setOnAction(e->MakeMove(butt));
                    GridPane.setConstraints(butt, i, j);
                    board.getChildren().add(butt);
                }
            }
        }

        return board;
    }


    public void setPlayersScore(GridPane PlayerScoreGridPane)
    {
        int i=1;
        PlayerScoreGridPane.setPadding(new Insets(5, 5, 5, 5));
        PlayerScoreGridPane.setVgap(8);
        PlayerScoreGridPane.setHgap(8);
        PlayerScoreGridPane.getChildren().get(0).setStyle("-fx-background-color:#efff11;"+"-fx-border-color: #cc0e1a");
        PlayerScoreGridPane.getChildren().get(1).setStyle("-fx-background-color:#efff11;"+"-fx-border-color: #cc0e1a");

        for (Player player:players) {
            Label name = new Label(player.getName());
            name.setStyle(" -fx-font-weight: bold;" +
                    "-fx-text-fill: #0407ce;");
            Label score = new Label(String.valueOf(player.getScore()));
            score.setStyle( "-fx-font-weight: bold;"
            +"-fx-text-fill: #02021a;");
            PlayerScoreGridPane.addRow(i, name, score);
            i++;

        }

    }

    public void setCurrentPlayer(Label PlayerNameLabel,Label CurrentPlayerIDLabel,Label CurrentPlayerTypeLabel,Label CurrentPlayerColorLabel)
    {
        PlayerNameLabel.setText(currentPlayer.getName());
        CurrentPlayerIDLabel.setText(String.valueOf(currentPlayer.getId()));
        CurrentPlayerTypeLabel.setText(String.valueOf(currentPlayer.getPlayerType()));
        CurrentPlayerColorLabel.setText(GameColor.getColor(currentPlayer.getColor()));
    }

    public void setCurrentMove(Label MoveNumberLabel)
    {
        MoveNumberLabel.setText(String.valueOf(moveNum));
    }

    private void MakeMove(Button butt)
    {
        butt.getStyleClass().add("button-pup");
        butt.setText("B");
    }
}
