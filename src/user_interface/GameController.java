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
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
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
    public static boolean xmLoaded =false;

    public void setGameWindow(Stage stage) {
        gameWindow = stage;
    }

    @FXML
    private Label IdLabel;

    @FXML
    private Label TypeLabel;

    @FXML
    private Label ColorLabel;


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

        builder.removeButtonsEffect();
        if(GameManager.gameRound > 0){
                restartGame();
            }else {
                logic.setHistoryMoves();
            }

        MakeAMoveButton.getStyleClass().clear();
        LeaveGameButton.disableProperty().setValue(false);
        LoadXmlFileButton.disableProperty().setValue(true);
        MakeAMoveButton.disableProperty().setValue(false);
        StartGameButton.disableProperty().setValue(true);

        setStartGame();
        GameManager.gameRound++;
        if (logic.getGameType().equals(eGameType.Advance)) {
            if (!logic.InitMoveCheck()) //first player check
            {
                noPossibleMovesAlert();
                findPlayerToNextMove();

            } else {
                if (logic.getCurrentPlayer().getPlayerType().equals(String.valueOf(ePlayerType.Computer))) {
                    makeComputerMove();
                }
            }
        }else{
            if (!logic.InitMoveCheck())
            {
                setGameOver();
            }
        }
    }



    private void restartGame(){

        clearGameWindow();
        disableHistoryView();

        try{
            gameManager.LoadGameFromXmlAndValidate(xmlFilePath);
            createGameView();
        }catch (XmlNotValidException ex){
            setInvalidXMLAlert(ex);
        }
            historyIndex = -1;
            logic.getHistoryMoves().clear();
            logic.setHistoryMoves();
    }

    private BoardButton getChosenButton(Point loc){
        return (BoardButton)builder.getNodeByRowColumnIndex(loc.getRow()+1,loc.getCol()+1,board);
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

                Platform.runLater(() -> {
                    BoardButton butt = getChosenButton(squareLocation);
                    butt.setChosenButtonEffect();
                });
                return squareLocation;
            }
            @Override
            protected void updateProgress(double workTodo,double max){
                updateMessage("Computer thinking...");
                super.updateProgress(workTodo,max);
            }
        };

        moveTask.setOnSucceeded(t -> {

            Point chosenPoint = moveTask.getValue();
                 ComputerThinkingLabel.textProperty().unbind();
                 ComputerProgressBar.visibleProperty().set(false);
                 ComputerThinkingLabel.visibleProperty().set(false);
                 System.out.println("Updating Board!");
                 logic.updateDataMove(chosenPoint);
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

private void enableHistoryView(){

    ReplayMovesLabel.visibleProperty().setValue(true);
    PrevButton.visibleProperty().setValue(true);
    PrevButton.disableProperty().setValue(false);
    NextButton.visibleProperty().setValue(true);
    historyIndex = -1;

}

private void disableHistoryView(){
    NextButton.visibleProperty().setValue(false);
    NextButton.disableProperty().setValue(true);
    PrevButton.visibleProperty().setValue(false);
    ReplayMovesLabel.visibleProperty().setValue(false);

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
        StartGameButton.disableProperty().setValue(false);

        clearGameWindow();
        enableHistoryView();
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
//            if(logic.getHistoryMoves()!=null){
//                logic.clearHistory();
//            }
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
        MoveNumberLabel.textProperty().unbind();
        MoveNumberLabel.setText("");
        StartGameButton.disableProperty().setValue(false);
        PrevButton.disableProperty().setValue(false);
    }

    @FXML
    void MakeAMoveButtonClicked(ActionEvent event) {
          builder.removeButtonsEffect();
        if (logic.getGameType().equals(eGameType.Advance))
            AdvanceMove();
        else {
            BasicMove();
        }
    }

    private void AdvanceMove()
    {
        int pointStatus;
        String value="";
       BoardButton butt = builder.getChosenButton();
        Point userPoint = builder.getChosenPoint();
        if (userPoint != null) {
            pointStatus = logic.isValidPoint(userPoint);
            if (pointStatus == GameLogic.GOOD_POINT) {
                    logic.updateDataMove(userPoint);
                    findPlayerToNextMove();
            }
            else if (pointStatus == GameLogic.NOT_IN_MARKER_ROW_AND_COLUMN)
            {
                value = logic.getGameBoard().getGameBoard()[userPoint.getRow()][userPoint.getCol()].getValue();
                logic.getGameBoard().getGameBoard()[userPoint.getRow()][userPoint.getCol()].setValue(value);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("You choose illegal square -the square needs to be in the marker raw or column");
                alert.showAndWait();
            }
            else if (pointStatus == GameLogic.NOT_PLAYER_COLOR)
            {
                value = logic.getGameBoard().getGameBoard()[userPoint.getRow()][userPoint.getCol()].getValue();
                logic.getGameBoard().getGameBoard()[userPoint.getRow()][userPoint.getCol()].setValue(value);
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

        butt.removeChosenButtonEffect();
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
        printWinnerBasicRetire(winner);
        LoadXmlFileButton.disableProperty().setValue(false);
        MakeAMoveButton.disableProperty().setValue(true);
        LeaveGameButton.disableProperty().setValue(true);
       // StartGameButton.disableProperty().setValue(false);
        clearGameWindow();
        enableHistoryView();
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
        PlayerNameLabel.setMaxWidth(300);
        builder.setPlayersScore(PlayerScoreGridPane,logic.getPlayers()); //after Game Starts

        if (logic.getGameType().equals(eGameType.Advance)){
            builder.clearPlayersView();
            setCurrentPlayer(logic.getCurrentPlayer());
        }
        else {
            setCurrentPlayerBasic(logic.getCurrentPlayer());
        }
      //  MoveNumberLabel.textProperty().bind(logic.gameMovesProperty().asString());
    }

    private void setCurrentPlayerBasic(Player currentPlayer)
    {
        PlayerNameLabel.setText(String.valueOf(currentPlayer.getTurn()));
        CurrentPlayerTypeLabel.setText(currentPlayer.playerTypeProperty().getValue());
    }

    public void LoadXmlFileButtonClicked() throws XmlNotValidException {
        if (GameManager.gameRound > 0) {
            if(!xmLoaded){
                logic.gameLogicClear();
            }
            disableHistoryView();
            clearGameWindow();
            logic.clearHistory();
            GameManager.gameRound = 0;
        }
        //boolean xmLoaded =false
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
            } catch (XmlNotValidException i_Exception) {
                setInvalidXMLAlert(i_Exception);
                xmLoaded = false;
            }
              if (xmLoaded) {
                 logic = gameManager.getGameLogic();
                 createGameView();
                 disableHistoryView();
              }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("XML File Load Error");
            alert.setHeaderText("Invalid File - cannot be loaded!");
            alert.showAndWait();
        }

    }


    private void createGameView(){

        StartGameButton.disableProperty().setValue(false);
        MoveNumberLabel.textProperty().bind(logic.gameMovesProperty().asString());
        board = builder.createBoard(logic.getGameBoard());
        borderPane.setCenter(board);
        if(logic.getGameType().equals(eGameType.Advance)){
            builder.createPlayersTable(logic.getPlayers());
            gamePlayers = builder.getPlayersTable();
            borderPane.setRight(gamePlayers);
            TypeLabel.visibleProperty().setValue(true);
            IdLabel.visibleProperty().setValue(true);
            ColorLabel.visibleProperty().setValue(true);
            CurrentPlayerTypeLabel.visibleProperty().setValue(true);

        }else if(logic.getGameType().equals(eGameType.Basic)){
            TypeLabel.visibleProperty().setValue(false);
            IdLabel.visibleProperty().setValue(false);
            ColorLabel.visibleProperty().setValue(false);
            CurrentPlayerTypeLabel.visibleProperty().setValue(false);
        }

    }


    private void createHistoryMoveView(){
        GameMove move = logic.getHistoryMoves().get(historyIndex);
        board = builder.createBoard(move.getGameBoard());
        borderPane.setCenter(board);
        setCurrentPlayer(move.getCurrentPlayer());
        MoveNumberLabel.setText(String.valueOf(move.getMoveNum()));
        builder.clearPlayersScoreView(PlayerScoreGridPane);
        builder.setPlayersScore(PlayerScoreGridPane,move.getPlayers());

    }

    private void setCurrentPlayer(Player currentPlayer)
    {
        if(logic.getGameType().equals(eGameType.Advance)){
            CurrentPlayerIDLabel.setText(String.valueOf(currentPlayer.getId()));
            CurrentPlayerColorLabel.setText(String.valueOf(currentPlayer.getPlayerColor()));
            CurrentPlayerTypeLabel.setText(currentPlayer.playerTypeProperty().getValue());
            PlayerNameLabel.setText(currentPlayer.getName());
        }else if(logic.getGameType().equals(eGameType.Basic)){
            PlayerNameLabel.setText(String.valueOf(currentPlayer.getTurn()));
        }
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

    public void printWinnerBasicRetire(String winner)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("you left the game");
        alert.setHeaderText("You left..so you lost...The winner is "+winner);
        alert.showAndWait();
    }

    public void BasicMove()
    {
        int pointStatus;
        boolean doSwitch = true;
        BoardButton butt = builder.getChosenButton();
        Point userPoint = builder.getChosenPoint();
        if (userPoint != null) {
            pointStatus = logic.isValidPoint(userPoint);
            if (pointStatus == GameLogic.GOOD_POINT) {
                logic.updateDataMove(userPoint);
                doSwitch = logic.switchPlayer();
                setCurrentPlayerBasic(logic.getCurrentPlayer());
                if (!doSwitch)
                {
                    setGameOver();
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

        butt.removeChosenButtonEffect();
    }


}
