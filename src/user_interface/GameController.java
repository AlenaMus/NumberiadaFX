package user_interface;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("View is now loaded!");
    }

    public void CreateGameBoardView() ///Board gameBoard,
     {
       // GridPane board = new GridPane();
        //board.setPadding(new Insets(10, 10, 10, 10));
        //board.setVgap(8);
       // board.setHgap(10);
      //   BorderPane.setCenter(board);
       //borderPane.setAlignment(board, Pos.CENTER);


    }



    public void MakeAMoveButtonClicked()
    {

    }

    public void RetireGameButtonClicked()
    {

    }

    public void PrevButtonClicked()
    {

    }

    public void NextButtonClicked()
    {

    }

    public void LoadXmlFileButtonClicked() //throws IOException
    {

//            try{
//                BasicGame logic = new BasicGame();
//                FileChooser fileChooser = new FileChooser();
//                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
//                fileChooser.getExtensionFilters().add(extFilter);
//                fileChooser.setTitle("Open Resource File");
//                File loadedFile =fileChooser.showOpenDialog(myStage);
//                if (loadedFile != null)
//                {
//
//                    logic.loadGame(loadedFile);
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoard.fxml"));
//                    Parent gameBoard = (Parent)loader.load();
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
//                }
//            }
//            catch(IOException e)
//            {
//
//            };
//

    }


   public void StartGameButtonClicked()
   {

   }


}
