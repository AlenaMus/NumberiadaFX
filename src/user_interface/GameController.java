package user_interface;

import game_engine.BasicGame;
import game_objects.GameColor;
import game_objects.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    GridPane board;
    NumberiadaBuilder builder;
    Stage gameWindow;

    public void setGameWindow(Stage stage)
    {
        gameWindow = stage;
    }


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
    private Label CurrentPlayerIDLabel;

    @FXML
    private Label PlayerNameLabel;

    @FXML
    private Label CurrentPlayerTypeLabel;

    @FXML
    private Label CurrentPlayerColorLabel;

    @FXML
    private Label MoveNumberLabel;

    @FXML
    private GridPane PlayerScoreGridPane;

    @FXML
    private Label nameScoreGridLabel;

    @FXML
    private Label scoreGridLabel;




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
        void StartGameButtonClicked(ActionEvent event)
        {

        }


    GridPane gamePlayers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        builder = new NumberiadaBuilder();
        board = builder.createBoard(10);
        borderPane.setCenter(board);
        builder.createPlayersTable();
        gamePlayers=builder.getPlayersTable();

        borderPane.setRight(gamePlayers); //loading xml file

        setStartGame();

    }

    public void setStartGame()
    {
        PlayerNameLabel.setMaxWidth(300);
        builder.setPlayersScore(PlayerScoreGridPane); //after Game Starts
        builder.setCurrentPlayer(PlayerNameLabel,CurrentPlayerIDLabel,CurrentPlayerTypeLabel,CurrentPlayerColorLabel);
        builder.setCurrentMove(MoveNumberLabel);

    }

    public void LoadXmlFileButtonClicked() throws IOException
    {

            try{
                BasicGame logic = new BasicGame();
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
                fileChooser.getExtensionFilters().add(extFilter);
                fileChooser.setTitle("Open Resource File");
                File loadedFile = fileChooser.showOpenDialog(gameWindow);
                if (loadedFile != null)
                {

//                        try
//                        {
//
                             logic.loadGameFromFile(loadedFile.getAbsolutePath());
//                        }
//                        catch(XmlNotValidException i_Exception)
//                        {
//                            Alert alert = new Alert(Alert.AlertType.ERROR);
//                            alert.setTitle("Invalid Game data file");
//                            alert.setHeaderText("Error reading xml file, the following errors were found");
//                            alert.setContentText(String.join(System.lineSeparator(), i_Exception.getValidationResult()));
//                            alert.showAndWait();
//                        }
                    }


                      FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
                      Parent gameBoard = (Parent)loader.load();
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
            //    }
            }
            catch(IOException e)
            {
                  e.printStackTrace();
            }
    }



}
