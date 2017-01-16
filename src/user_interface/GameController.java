package user_interface;

import game_engine.GameLogic;
import game_engine.GameManager;
import game_objects.Player;
import game_objects.Point;
import game_objects.ePlayerType;
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
    private GridPane gamePlayers;
    private String xmlFilePath;
    private GameManager gameManager = new GameManager();

    public void setGameWindow(Stage stage) {
        gameWindow = stage;
    }

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button MakeAMoveButton;


    @FXML
    private Button ExitGameButton;

    @FXML
    private Button LeaveGameButton;

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

        if(GameLogic.gameRound > 0){
            clearGameWindow();
            try{
                gameManager.LoadGameFromXmlAndValidate(xmlFilePath);
                logic = gameManager.getGameLogic();
                createGameView();
            }catch (XmlNotValidException ex){
                setInvalidXMLAlert(ex);
            }
        }
        MakeAMoveButton.disableProperty().setValue(false);
        LeaveGameButton.disableProperty().setValue(false);
        LoadXmlFileButton.disableProperty().setValue(true);
        logic.initGame();
        setStartGame();
        GameLogic.gameRound++;
        if(!logic.InitMoveCheck())
        {
            noPossibleMovesAlert();
            findPlayerToNextMove();
        }else if(logic.getCurrentPlayer().getPlayerType().equals(String.valueOf(ePlayerType.Computer))){
            makeComputerMove();
        }

    }

    private void makeComputerMove(){
        logic.makeComputerMove();
        findPlayerToNextMove();
    }

    private void findPlayerToNextMove() {
        if (!logic.isGameOver()) {
            boolean hasMove = logic.switchPlayer();
            setCurrentPlayer(logic.getCurrentPlayer());
            while (!hasMove) {
                noPossibleMovesAlert();
                hasMove = logic.switchPlayer();
                setCurrentPlayer(logic.getCurrentPlayer());
            }
            if(logic.getCurrentPlayer().getPlayerType().equals(String.valueOf(ePlayerType.Computer))){
                makeComputerMove();
            }
        } else {
            setGameOver();
        }
    }

    private void setGameOver()
    {
        String winnerMessage = logic.getWinner();
        String statistics = logic.gameOver();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("GAME OVER !!!");
        alert.setHeaderText(winnerMessage);
        alert.setContentText(String.join(System.lineSeparator(),statistics));
        alert.showAndWait();
        LoadXmlFileButton.disableProperty().setValue(false);
        MakeAMoveButton.disableProperty().setValue(true);
        LeaveGameButton.disableProperty().setValue(true);
        clearGameWindow();
    }

    @FXML
    private void ExitGameButtonClicked(ActionEvent event){
    }
    private void clearGameWindow()
    {
        builder.clearBoard();
        borderPane.setCenter(null);
        builder.clearPlayersScoreView(PlayerScoreGridPane);
        clearCurrentPlayerView();
    }

    @FXML
    void MakeAMoveButtonClicked(ActionEvent event) {
        if (logic.getGameType().toString() == "Advance")
            AdvanceMove();
        else {
            BasicMove();
        }
    }


    public void AdvanceMove()
    {
        int pointStatus;
        Point userPoint = builder.getChosenPoint();
        if (userPoint != null) {
            pointStatus = logic.isValidPoint(userPoint);
            if (pointStatus == GameLogic.GOOD_POINT) {
                logic.makeHumanMove(userPoint);
                findPlayerToNextMove();
            }
            else if (pointStatus == GameLogic.NOT_IN_MARKER_ROW_AND_COLUMN)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("You choose illegal square -the square needs to be in the marker raw or column");
                alert.showAndWait();
            }
            else if (pointStatus == GameLogic.NOT_PLAYER_COLOR)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("You choose illegal square - the square is not in your color!");
                alert.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("YOU DIDNT CHOOSE A SQUARE!");
            alert.showAndWait();
        }
    }


    public void BasicMove()
    {
        int pointStatus;
        boolean doSwitch = true;
        Point userPoint = builder.getChosenPoint();
        if (userPoint != null) {
            pointStatus = logic.isValidPoint(userPoint);
            if (pointStatus == GameLogic.GOOD_POINT) {
                logic.makeHumanMove(userPoint);
                doSwitch = logic.switchPlayer();
                setCurrentPlayerBasic(logic.getCurrentPlayer());
                if (!doSwitch)
                {
                    String winner = logic.gameOver();
                    printWinnerBasic(winner);
                }
            }
            else if (pointStatus == GameLogic.NOT_IN_MARKER_ROW_BASIC)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("You choose illegal square -the square needs to be in the marker raw ");
                alert.showAndWait();
            }
            else if (pointStatus == GameLogic.NOT_IN_MARKER_COL_BASIC)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("You choose illegal square -the square needs to be in the marker column");
                alert.showAndWait();
            }
            else if (pointStatus == GameLogic.MARKER_SQUARE_BASIC)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("You choose illegal square -cannot choose marker square");
                alert.showAndWait();
            }
            else if (pointStatus == GameLogic.EMPTY_SQUARE_BASIC)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("You choose illegal square -cannot choose empty square");
                alert.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("YOU DIDNT CHOOSE A SQUARE!");
            alert.showAndWait();
        }
    }

    public void printWinnerBasic(String winner)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("GAME OVER !!!");
        alert.setHeaderText("The winner is "+winner);
        alert.showAndWait();
    }

    @FXML
    void RetireGameButtonClicked(ActionEvent event) {
        if (logic.getGameType().toString() =="Advance")
            AdvanceRetire();
        else {
            BasicRetire();
        }
    }
    void AdvanceRetire()
    {
        logic.playerRetire();
        if(!GameLogic.isEndOfGame){
            builder.clearPlayersScoreView(PlayerScoreGridPane);
            builder.setPlayersScore(PlayerScoreGridPane);
            findPlayerToNextMove();
        }else{
            setGameOver();
        }
    }

    public void BasicRetire()
    {
        String winner =logic.playerRetire();
        printWinnerBasic(winner);
    }
    @FXML
    void NextButtonClicked(ActionEvent event) {

    }

    @FXML
    void PrevButtonClicked(ActionEvent event) {

    }





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        board = new GridPane();
        StartGameButton.disableProperty().setValue(true);
        MakeAMoveButton.disableProperty().setValue(true);
        LeaveGameButton.disableProperty().setValue(true);
    }

    private void setStartGame() {


        builder.clearPlayersView();
        borderPane.setRight(null);
        PlayerNameLabel.setMaxWidth(300);
        builder.setPlayersScore(PlayerScoreGridPane); //after Game Starts
        if (logic.getGameType().toString() == "Advance")
            setCurrentPlayer(logic.getCurrentPlayer());
        else {
            setCurrentPlayerBasic(logic.getCurrentPlayer());
        }
        MoveNumberLabel.textProperty().bind(logic.gameMovesProperty().asString());

    }

    public void LoadXmlFileButtonClicked() throws XmlNotValidException {
        boolean xmLoaded = false;
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Open Resource File");
        File loadedFile = fileChooser.showOpenDialog(gameWindow);
        if (loadedFile != null) {
            try {
                xmlFilePath = loadedFile.getAbsolutePath();
                gameManager.LoadGameFromXmlAndValidate(xmlFilePath);
                xmLoaded = true;
                logic = gameManager.getGameLogic();
            } catch (XmlNotValidException i_Exception) {
                setInvalidXMLAlert(i_Exception);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("XML File Load Error");
            alert.setHeaderText("Invalid File - cannot be loaded!");
            alert.showAndWait();
        }
        if (xmLoaded) {
            createGameView();
        }
    }


    private void createGameView(){
        StartGameButton.disableProperty().setValue(false);
        board = builder.createBoard(logic.getGameBoard());
        borderPane.setCenter(board);
        builder.createPlayersTable(logic.getPlayers());
        gamePlayers = builder.getPlayersTable();
        borderPane.setRight(gamePlayers);
    }
    private void setCurrentPlayer(Player currentPlayer)
    {
        PlayerNameLabel.setText(currentPlayer.getName());
        CurrentPlayerIDLabel.setText(String.valueOf(currentPlayer.getId()));
        CurrentPlayerColorLabel.setText(String.valueOf(currentPlayer.getPlayerColor()));
        CurrentPlayerTypeLabel.setText(currentPlayer.playerTypeProperty().getValue());
    }

    private void setCurrentPlayerBasic(Player currentPlayer)
    {
        PlayerNameLabel.setText(String.valueOf(currentPlayer.getTurn()));
        CurrentPlayerTypeLabel.setText(currentPlayer.playerTypeProperty().getValue());
    }

    private void clearCurrentPlayerView()
    {
        PlayerNameLabel.setText("");
        CurrentPlayerIDLabel.setText("");
        CurrentPlayerColorLabel.setText("");
        CurrentPlayerTypeLabel.setText("");
    }


    private void setInvalidXMLAlert(XmlNotValidException ex){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Game data file");
        alert.setHeaderText("Error reading xml file, the following errors were found");
        alert.setContentText(String.join(System.lineSeparator(), ex.getValidationResult()));
        alert.showAndWait();
    }

    private void noPossibleMovesAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No possible moves ");
        alert.setHeaderText(String.format("%s, you have no available moves!\n" +
                " Skipped to next player !\n" +
                " Please wait to your next turn :)", logic.getCurrentPlayer().getName()));
        alert.showAndWait();
    }

}
