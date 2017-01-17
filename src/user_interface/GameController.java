package user_interface;

import game_engine.GameLogic;
import game_engine.GameManager;
import game_engine.eGameType;
import game_objects.*;
import game_validation.XmlNotValidException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
    private int historyIndex = -1;
    private GameManager gameManager = new GameManager();

    public void setGameWindow(Stage stage) {
        gameWindow = stage;
    }

    @FXML
    private Label ComputerThinkingLabel;

    @FXML
    private ProgressBar ComputerProgressBar;

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
        PrevButton.disableProperty().setValue(true);
        NextButton.disableProperty().setValue(true);
        MakeAMoveButton.disableProperty().setValue(false);
        LeaveGameButton.disableProperty().setValue(false);
        LoadXmlFileButton.disableProperty().setValue(true);
        logic.initGame();
        setStartGame();
        GameLogic.gameRound++;

        if(!logic.InitMoveCheck()) //first player check
        {
            noPossibleMovesAlert();
            findPlayerToNextMove();

        }else{
            if(logic.getCurrentPlayer().getPlayerType().equals(String.valueOf(ePlayerType.Computer))){
                makeComputerMove();
            }
        }
    }

    private void makeComputerMove() {

        ComputerProgressBar.visibleProperty().set(true);
        ComputerThinkingLabel.visibleProperty().set(true);
        Task<Point> moveTask = new Task<Point>() {
            Point squareLocation = null;


            @Override
            protected Point call() throws Exception {
                int i;

                squareLocation = logic.makeComputerMove();
                for(i=1;i < 10; i++){
                    updateProgress(i,10);
                    Thread.sleep(80);
                }
                    Thread.sleep(500);
                return squareLocation;
            }
            @Override
            protected void updateProgress(double workTodo,double max){
                updateMessage("Computer thinking...");
                super.updateProgress(workTodo,max);
            }
        };

        moveTask.setOnSucceeded(t -> {
            System.out.println("Updating Board!");
            logic.updateDataMove(moveTask.getValue());
                 ComputerThinkingLabel.textProperty().unbind();
                 ComputerProgressBar.visibleProperty().set(false);
                 ComputerThinkingLabel.visibleProperty().set(false);
            try{
                Thread.sleep(200);
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
                System.out.println("Finding next player after computer");
                findPlayerToNextMove();
        });

        moveTask.setOnFailed(t -> {
            System.out.println("Failed to perform task !!");
            ComputerThinkingLabel.textProperty().unbind();
            ComputerProgressBar.visibleProperty().set(false);
            ComputerThinkingLabel.visibleProperty().set(false);
            System.out.println("Finding next player after computer");
            findPlayerToNextMove();
        });

        ComputerProgressBar.progressProperty().bind(moveTask.progressProperty());
        ComputerThinkingLabel.textProperty().bind(moveTask.messageProperty());

        moveTask.valueProperty().addListener((observable, oldValue, newValue) ->  {
            System.out.println(String.format("Value reutrned is %d %d",newValue.getRow(),newValue.getCol()));});
        Thread move = new Thread(moveTask);
        move.start();

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

        MoveNumberLabel.textProperty().unbind();
        LoadXmlFileButton.disableProperty().setValue(false);
        MakeAMoveButton.disableProperty().setValue(true);
        LeaveGameButton.disableProperty().setValue(true);
        PrevButton.disableProperty().setValue(false);
        PrevButton.visibleProperty().setValue(true);
        NextButton.visibleProperty().setValue(true);
        clearGameWindow();

    }

    @FXML
    private void PrevHistoryButtonClicked(){
        if(historyIndex == -1){
            historyIndex = logic.getHistoryMoves().size()-1;
            NextButton.disableProperty().setValue(false);
        }

    if(historyIndex > 1){
        createHistoryMoveView();
        historyIndex--;
    }

    if(historyIndex == 0){
        createHistoryMoveView();
        PrevButton.disableProperty().setValue(true);
    }
    }

    @FXML
    private void NextHistoryButtonClicked(){
        if(historyIndex < logic.getHistoryMoves().size()-1){
            historyIndex++;
            createHistoryMoveView();
        }

        if(historyIndex == logic.getHistoryMoves().size()-1){
            createHistoryMoveView();
            NextButton.disableProperty().setValue(true);
        }
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
        if (logic.getGameType().equals(eGameType.Advance))
            AdvanceMove();
        else {
            BasicMove();
        }
    }

    private void AdvanceMove()
    {
        int pointStatus;
        Point userPoint = builder.getChosenPoint();
        if (userPoint != null) {
            pointStatus = logic.isValidPoint(userPoint);
            if (pointStatus == GameLogic.GOOD_POINT) {
                    logic.updateDataMove(userPoint);
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


    private void BasicMove()
    {
        int pointStatus;
        boolean doSwitch = true;
        Point userPoint = builder.getChosenPoint();
        if (userPoint != null) {
            pointStatus = logic.isValidPoint(userPoint);
            if (pointStatus == GameLogic.GOOD_POINT) {
                logic.updateDataMove(userPoint);
                doSwitch = logic.switchPlayer();
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

    private void printWinnerBasic(String winner)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("GAME OVER !!!");
        alert.setHeaderText("The winner is "+ winner);
        alert.showAndWait();
    }

    @FXML
    void RetireGameButtonClicked(ActionEvent event) {
        if (logic.getGameType().toString().equals("Advance"))
            AdvanceRetire();
        else {
            BasicRetire();
        }
    }
    private void AdvanceRetire()
    {
        logic.playerRetire();
        if(!GameLogic.isEndOfGame){
            builder.clearPlayersScoreView(PlayerScoreGridPane);
            builder.setPlayersScore(PlayerScoreGridPane,logic.getPlayers());
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
        builder.setPlayersScore(PlayerScoreGridPane,logic.getPlayers()); //after Game Starts
        setCurrentPlayer(logic.getCurrentPlayer());
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


    private void createHistoryMoveView(){
        GameMove move = logic.getHistoryMoves().get(historyIndex);
        move.getChosenMove().setColor(7);
        board = builder.createBoard(move.getGameBoard());
        borderPane.setCenter(board);
        setCurrentPlayer(move.getCurrentPlayer());
        MoveNumberLabel.setText(String.valueOf(move.getMoveNum()));
        builder.setPlayersScore(PlayerScoreGridPane,move.getPlayers());

    }

    private void setCurrentPlayer(Player currentPlayer)
    {
        PlayerNameLabel.setText(currentPlayer.getName());
        CurrentPlayerIDLabel.setText(String.valueOf(currentPlayer.getId()));
        CurrentPlayerColorLabel.setText(String.valueOf(currentPlayer.getPlayerColor()));
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
