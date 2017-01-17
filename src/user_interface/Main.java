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
      //   FXMLLoader fxmlLoader = new FXMLLoader();
         Parent root = FXMLLoader.load(getClass().getResource("Numberiada.fxml"));
         primaryStage.setTitle("Numberiada");
         Scene scene = new Scene(root, 1000,1000);
         scene.getStylesheets().add("user_interface/boardStyle.css");
         primaryStage.setScene(scene);
         primaryStage.show();
    }



}
