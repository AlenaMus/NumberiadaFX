package user_interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("View is now loaded!");
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
