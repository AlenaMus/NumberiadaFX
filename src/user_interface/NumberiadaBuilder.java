package user_interface;

import game_objects.GameColor;
import game_objects.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import game_objects.ePlayerType;

import java.util.ArrayList;
import java.util.List;

public class NumberiadaBuilder {


    private static final int squareSize = 20;

    private GridPane board;
    private GridPane m_players;
    //private TableView<Player> m_players = new TableView<>();
    //private TextField PlayerName, PlayerID, Type,Color;
    List<Player> players = new ArrayList<>();

    public void setPlayers() {
       players.add(new Player(ePlayerType.HUMAN,"Kate",2, 555));
        players.add(new Player(ePlayerType.HUMAN,"Dana",6, 323));
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
        m_players.setVgap(5);
        m_players.setHgap(5);
        Label name = new Label("Player Name");
        name.setStyle("-fx-font-weight: bold;" +
                "-fx-text-fill: #AB4642;");
        GridPane.setConstraints(name, 0, 0);
        Label id = new Label("ID");
        id.setStyle("-fx-font-weight: bold;" +
                "-fx-text-fill: #AB4642;");
        GridPane.setConstraints(id, 1, 0);
        Label type = new Label("Player Type");
        type.setStyle("-fx-font-weight: bold;" +
                "-fx-text-fill: #AB4642;");
        GridPane.setConstraints(type, 2, 0);
        Label color = new Label("Color");
        color.setStyle("-fx-font-weight: bold;" +
                "-fx-text-fill: #AB4642;");
        GridPane.setConstraints(color, 3,0);
        m_players.getChildren().addAll(name,id,type,color);
        int i=1;
//
//        for (Player player:players) {
//
//            name.setText(player.getName());
//            GridPane.setConstraints(name, i, 0);
//            id.setText(String.valueOf(player.getId()));
//            GridPane.setConstraints(id, i, 1);
//            type.setText(player.getPlayerType().toString());
//            GridPane.setConstraints(type, i, 2);
//            color.setText(String.valueOf(player.getColor()));
//            GridPane.setConstraints(color, i, 3);
//            m_players.getChildren().addAll(name,id,type,color);
//            i++;
//        }






//        TableColumn<Player, String> nameColumn = new TableColumn<>("Name");
//        nameColumn.setMinWidth(100);
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
//
//        TableColumn<Player, Integer> idColumn = new TableColumn<>("PlayerID");
//        idColumn.setMinWidth(50);
//        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//
//        TableColumn<Player, ePlayerType> typeColumn = new TableColumn<>("Type");
//        typeColumn.setMinWidth(80);
//        typeColumn.setCellValueFactory(new PropertyValueFactory<>("playerType"));
//
//        TableColumn<Player, Integer> colorColumn = new TableColumn<>("Color");
//        colorColumn.setMinWidth(80);
//        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
//
//        m_players.getColumns().addAll(nameColumn,idColumn,typeColumn,colorColumn);

//        for (Player player:players) {
//            m_players.getItems().add(player);
//        }


    }

    public GridPane createBoard(int size) {
        board = new GridPane();
        board.setPrefSize(400,400);
        //  board.setGridLinesVisible(true);
        board.setPadding(new Insets(10, 0, 10, 0));
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

    private void MakeMove(Button butt)
    {
        butt.getStyleClass().add("button-pup");
        butt.setText("B");
    }
}
