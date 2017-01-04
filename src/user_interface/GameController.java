package user_interface;

import game_objects.GameColor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import game_objects.GameColor;
import javafx.scene.text.TextAlignment;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {


    private GridPane board = new GridPane();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        board.setPadding(new Insets(20, 0, 10, 0));
        board.setVgap(2);
        board.setHgap(2);
       // Label nameLabel = new Label("Username:");
      //  GridPane.setConstraints(nameLabel, 0, 0);
       // Label passLabel = new Label("Password:");
      //  Button butt = new Button("BBB");
      //  GridPane.setConstraints(butt,2,2);
     //   GridPane.setConstraints(passLabel, 1, 0);
      //  board.getChildren().addAll(nameLabel,passLabel,butt);
        createBoard(5);
        borderPane.setCenter(board);
        System.out.println("View is now loaded!");
    }

    private void createBoard(int size) {
     //  board.setGridLinesVisible(true);

        for(int i = 0; i < size; i++) {
            ColumnConstraints column = new ColumnConstraints(20);
            board.getColumnConstraints().add(column);
        }

        for(int i = 0; i < size; i++) {
            RowConstraints row = new RowConstraints(20);
            board.getRowConstraints().add(row);
        }

        int i,j;
        Label lab = new Label("");
        lab.setPrefSize(20,20);
        lab.setAlignment(Pos.CENTER);

        board.add(lab,0,0);

        for(i=1;i<=size;i++)
        {
            lab = new Label(Integer.toString(i));
            lab.setPrefSize(20,20);
            lab.setAlignment(Pos.CENTER);
            board.add(lab,i,0);
        }

       for (j =1;j <=size; j++) {
           for ( i=0 ; i <=size; i++) {
                if (i==0) {
                    lab = new Label(Integer.toString(j));
                    lab.setPrefSize(20,20);
                    lab.setAlignment(Pos.CENTER);
                    board.add(lab,i,j);
               }
                else
                {
                    Button butt = new Button("A");
                    butt.setPrefSize(20,20);
                    butt.getStyleClass().add("button-blue");
                    GridPane.setConstraints(butt, i, j);
                    board.getChildren().add(butt);
                }
            }
        }
    }

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private BorderPane borderPane;

        @FXML
        private Button MakeAMoveButton;

        @FXML
        private Button RetireGameButton;

        @FXML
        private Button PrevButton;

        @FXML
        private Button NextButton;

        @FXML
        private Button LoadXmlFileButton;

        @FXML
        private Button StartGameButton;

        @FXML
        private Label PlayerIDLabel;

        @FXML
        private Label PlayerNameLabel;

        @FXML
        private Label PlayerTypeLabel;

        @FXML
        private Label PlayerColorLabel;

        @FXML
        private Label MoveNumberLabel;

        @FXML
        void LoadXmlFileButtonClicked(ActionEvent event) {

        }

        @FXML
        void MakeAMoveButtonClicked(ActionEvent event) {

        }

        @FXML
        void NextButtonClicked(ActionEvent event) {

        }

        @FXML
        void PrevButtonClicked(ActionEvent event) {

        }

        @FXML
        void RetireGameButtonClicked(ActionEvent event) {

        }

        @FXML
        void StartGameButtonClicked(ActionEvent event) {

        }





    public void LoadXmlFileButtonClicked() //throws IOException
    {

//            try{
//                BasicGame logic = new BasicGame();
//                FileChooser fileChooser = new FileChooser();
//                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
//                fileChooser.getExtensionFilters().add(extFilter);
//                fileChooser.setTitle("Open Resource File");
//                File loadedFile =fileChooser.showOpenDialog(myStage);
//                if (loadedFile != null)
//                {
//
//                    logic.loadGame(loadedFile);
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoard.fxml"));
//                    Parent gameBoard = (Parent)loader.load();
//                    GameController controller = (GameController)loader.getController();
//                    ((Node)event.getSource()).getScene().getWindow().hide();
//                    controller.logic=logic;
//                    controller.logic.setCurrPlayer(controller.logic.getPlayersList().get(logic.getPlayerIndex()));
//                    controller.printPlayersTiles();
//                    controller.printBoard();
//                    Scene scene=new Scene(gameBoard);
//                    Stage stage=new Stage();
//                    stage.setScene(scene);
//                    stage.show();
//                    Button b= new Button();
//                    //controller.computer();
//                    throw new IOException("your message");
//                }
//            }
//            catch(IOException e)
//            {
//
//            };
//

    }





}
