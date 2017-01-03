package user_interface;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
//update check
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        GameController NumberiadaBuiler = new GameController(primaryStage);
        NumberiadaBuiler.Execute();


    }



}
