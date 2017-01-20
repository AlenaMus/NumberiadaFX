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
         Parent root = FXMLLoader.load(getClass().getResource("Numberiada.fxml"));
       //  root.setId("screen");
       //  root.getStyleClass().add("screen");

         primaryStage.setTitle("Numberiada");
         Scene scene = new Scene(root,1000,800);
         scene.getStylesheets().add("user_interface/boardStyle.css");
         primaryStage.setScene(scene);
         primaryStage.show();
    }



}
