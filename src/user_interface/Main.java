package user_interface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Numberiada");
        Scene scene = new Scene(root, 800, 800);
        GameController theGame = new GameController();
        theGame.setGameWindow(primaryStage);
        scene.getStylesheets().add("user_interface/boardStyle.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



}
