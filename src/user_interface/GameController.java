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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Optional;
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
    private Label ReplayMovesLabel;

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

        if(GameManager.gameRound > 0){
           restartGame();
        }else{
            logic.setHistoryMoves();
        }

        MakeAMoveButton.getStyleClass().clear();
        LeaveGameButton.disableProperty().setValue(false);
        LoadXmlFileButton.disableProperty().setValue(true);
        MakeAMoveButton.disableProperty().setValue(false);
        StartGameButton.disableProperty().setValue(true);
        setStartGame();
        GameManager.gameRound++;

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



    private void restartGame(){
        historyIndex = -1;
        clearGameWindow();
        try{
            gameManager.LoadGameFromXmlAndValidate(xmlFilePath);
            createGameView();
        }catch (XmlNotValidException ex){
            setInvalidXMLAlert(ex);
        }
        logic.getHistoryMoves().clear();
        logic.setHistoryMoves();

        NextButton.visibleProperty().setValue(false);
        PrevButton.visibleProperty().setValue(false);
        ReplayMovesLabel.visibleProperty().setValue(false);
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
                    Thread.sleep(100);
                }
                    Thread.sleep(300);
                return squareLocation;
            }
            @Override
            protected void updateProgress(double workTodo,double max){
                updateMessage("Computer thinking...");
                super.updateProgress(workTodo,max);
            }
        };

        moveTask.setOnSucceeded(t -> {

                 ComputerThinkingLabel.textProperty().unbind();
                 ComputerProgressBar.visibleProperty().set(false);
                 ComputerThinkingLabel.visibleProperty().set(false);
                 System.out.println("Updating Board!");
                 logic.updateDataMove(moveTask.getValue());
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
            System.out.println(String.format("Value returned is %d %d",newValue.getRow(),newValue.getCol()));});
        Thread move = new Thread(moveTask);
        move.start();

//        Point squareLocation;
//        squareLocation = logic.makeComputerMove();
//        logic.updateDataMove(squareLocation);
//        findPlayerToNextMove();
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


        StartGameButton.disableProperty().setValue(false);
        MoveNumberLabel.textProperty().unbind();
        LoadXmlFileButton.disableProperty().setValue(false);
        MakeAMoveButton.disableProperty().setValue(true);
        LeaveGameButton.disableProperty().setValue(true);
        ReplayMovesLabel.visibleProperty().setValue(true);
        PrevButton.visibleProperty().setValue(true);
        NextButton.visibleProperty().setValue(true);
        historyIndex = -1;
        clearGameWindow();

    }

    @FXML
    private void PrevHistoryButtonClicked(){
        if(historyIndex == -1){
            PrevButton.disableProperty().setValue(false);
            historyIndex = logic.getHistoryMoves().size()-1;
            NextButton.disableProperty().setValue(false);
            createHistoryMoveView();
        }else if(historyIndex >= 1){
                historyIndex--;
                createHistoryMoveView();
            }

        }



    @FXML
    private void NextHistoryButtonClicked(){
        if(historyIndex < logic.getHistoryMoves().size()-1){
            historyIndex++;
            createHistoryMoveView();
        }
    }

    @FXML
    private void ExitGameButtonClicked(ActionEvent event){
        Alert exitWindow = new Alert(Alert.AlertType.CONFIRMATION);
        exitWindow.setTitle("Exit Game");
        exitWindow.setHeaderText("Do you want to exit Numberiada Game?");

        Optional<ButtonType> result = exitWindow.showAndWait();
        if( result.get() == ButtonType.OK){
            logic.clearHistory();
            Platform.exit();
        }else{
            exitWindow.hide();
        }
    }

    private void clearGameWindow()
    {
        builder.clearBoard();
        borderPane.setCenter(null);
        builder.clearPlayersScoreView(PlayerScoreGridPane);
        clearCurrentPlayerView();
        NextButton.visibleProperty().setValue(true);
        NextButton.disableProperty().setValue(false);

        PrevButton.disableProperty().setValue(false);
        PrevButton.visibleProperty().setValue(true);
        ReplayMovesLabel.visibleProperty().setValue(true);

    }

    @FXML
    void MakeAMoveButtonClicked(ActionEvent event) {
        System.out.print("Move clicked\n");
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
            alert.setHeaderText("YOU DIDN'T CHOOSE A SQUARE!");
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
        MakeAMoveButton.disableProperty().setValue(true);
        StartGameButton.disableProperty().setValue(true);
        LeaveGameButton.disableProperty().setValue(true);
        ComputerProgressBar.visibleProperty().setValue(false);
        ReplayMovesLabel.visibleProperty().setValue(false);
        PrevButton.visibleProperty().setValue(false);
        NextButton.visibleProperty().setValue(false);
        PrevButton.disableProperty().setValue(true);
        NextButton.disableProperty().setValue(true);
    }

    private void setStartGame() {
        logic.setFirstPlayer();
        builder.clearPlayersView();
        PlayerNameLabel.setMaxWidth(300);
        builder.setPlayersScore(PlayerScoreGridPane,logic.getPlayers()); //after Game Starts
        setCurrentPlayer(logic.getCurrentPlayer());
        MoveNumberLabel.textProperty().bind(logic.gameMovesProperty().asString());
    }

    public void LoadXmlFileButtonClicked() throws XmlNotValidException {
        if (GameManager.gameRound > 0) {
            logic.clearHistory();
        }
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
        board = builder.createBoard(move.getGameBoard());
        borderPane.setCenter(board);
        if(move.getChosenMove()!=null){
            move.getChosenMove().colorProperty().set(GameColor.PURPLE);
        }
        setCurrentPlayer(move.getCurrentPlayer());
        MoveNumberLabel.setText(String.valueOf(move.getMoveNum()));
        builder.clearPlayersScoreView(PlayerScoreGridPane);
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
