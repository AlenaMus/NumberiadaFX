package user_interface;

import game_engine.GameLogic;
import game_engine.GameManager;
import game_objects.Point;
import game_validation.ValidationResult;
import game_validation.XmlNotValidException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private GridPane board;
    private NumberiadaBuilder builder = new NumberiadaBuilder();
    private Stage gameWindow;
    private GameLogic logic;

    public void setGameWindow(Stage stage) {
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
    void StartGameButtonClicked(ActionEvent event) {
        MakeAMoveButton.disableProperty().setValue(false);
        logic.initGame();
        setStartGame();
    }

    @FXML
    void MakeAMoveButtonClicked(ActionEvent event) {
        boolean isValidMove = false ;
        int pointStatus;
        Point userPoint = builder.getChosenPoint();
        if (userPoint != null)
        {
            pointStatus = logic.isValidPoint(userPoint);
            if (pointStatus == logic.GOOD_POINT) {
                //logic.makeComputerMove();
                logic.makeHumanMove(userPoint);
                logic.setCurrentPlayer(logic.getPlayers().get(1));
                builder.setCurrentPlayer(logic.getCurrentPlayer(),PlayerNameLabel, CurrentPlayerIDLabel, CurrentPlayerTypeLabel, CurrentPlayerColorLabel);
            }
            else if (pointStatus == logic.NOT_IN_MARKER_ROW_AND_COLUMN)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("You choose illegal square -the square needs to be in the marker raw or column");
                alert.showAndWait();
            }
            else if (pointStatus == logic.NOT_PLAYER_COLOR)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("You choose illegal square - the square is not in your color!");
                alert.showAndWait();
            }

        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("YOU DIDNT CHOOSE A SQUARE YOU DUMB FUCK");
            alert.showAndWait();
        }
    }

    @FXML
    void NextButtonClicked(ActionEvent event) {

    }

    @FXML
    void PrevButtonClicked(ActionEvent event) {

    }

    @FXML
    void RetireGameButtonClicked(ActionEvent event) {
        logic.playerRetire();

    }




   private GridPane gamePlayers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
          board = new GridPane();
          StartGameButton.disableProperty().setValue(true);
          MakeAMoveButton.disableProperty().setValue(true);

    }

    private void setStartGame() {

        int size = logic.getGameBoard().GetBoardSize();
        logic.getGameBoard().getGameBoard()[size-1][size-1].setColor(5);
        logic.getGameBoard().getGameBoard()[size-1][size-1].setValue("00");

        PlayerNameLabel.setMaxWidth(300);
        builder.setPlayersScore(PlayerScoreGridPane); //after Game Starts
        builder.setCurrentPlayer(logic.getCurrentPlayer(),PlayerNameLabel, CurrentPlayerIDLabel, CurrentPlayerTypeLabel, CurrentPlayerColorLabel);
        builder.setCurrentMove(MoveNumberLabel,logic.getMoves());

    }

    public void LoadXmlFileButtonClicked() throws XmlNotValidException {
        boolean xmLoaded = false;

        GameManager gameManager = new GameManager();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Open Resource File");
        File loadedFile = fileChooser.showOpenDialog(gameWindow);
        if (loadedFile != null) {
            try {
                gameManager.LoadGameFromXmlAndValidate(loadedFile.getAbsolutePath());
                xmLoaded = true;
                logic = gameManager.getGameLogic();

                StartGameButton.disableProperty().setValue(false);
            } catch (XmlNotValidException i_Exception) {
                //  AlertPopup.display("XML Data Reading Error",GameLogic.validationResult.toString());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Game data file");
                alert.setHeaderText("Error reading xml file, the following errors were found");
                alert.setContentText(String.join(System.lineSeparator(), i_Exception.getValidationResult()));
                alert.showAndWait();
            }
        } else {
                ValidationResult res = new ValidationResult();
                res.add("XML File Load Error: Invalid File - cannot be loaded!");
        }

        if (xmLoaded) {

            board = builder.createBoard(logic.getGameBoard());
            borderPane.setCenter(board);
            builder.createPlayersTable(logic.getPlayers());
            gamePlayers = builder.getPlayersTable();
            borderPane.setRight(gamePlayers);

        }
    }
}
