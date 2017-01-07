package game_engine;

//import javax.xml.bind.Marshaller;


import game_validation.ValidationResult;
import game_validation.XmlNotValidException;
import jaxb.schema.generated.GameDescriptor;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.InputStream;

public class GameManager  {

    private static final int LOAD_GAME = 1;
    private static final int START_GAME = 2;
    private static final int EXIT_GAME = 3;

    private static final int SHOW_BOARD_AND_CURRENT_PLAYER = 1;
    private static final int MAKE_A_MOVE = 2;
    private static final int SHOW_STATISTICS = 3;
    private static final int LEAVE_GAME = 4;


    private static int gameRound = 0;
    private boolean isLoadedGame = false;
    protected eGameType gameType;
    private GameLogic gameLogic = null;
    private GameDescriptor loadedGame =null;



    public GameDescriptor loadGameFromFile(String fileName) throws XmlNotValidException {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            InputStream xmlFileInputStream = BasicGame.class.getResourceAsStream("/xml_resources/Numberiada.xsd");
            Source schemaSource = new StreamSource(xmlFileInputStream);
            Schema schema = schemaFactory.newSchema(schemaSource);
            JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(schema);
            GameDescriptor JaxbGame;
            JaxbGame = (GameDescriptor) unmarshaller.unmarshal(new File(fileName));
            return JaxbGame;
        }
        catch (JAXBException e) {

            ValidationResult validationResult = new ValidationResult();
            validationResult.add(String.format("file %1$s xml is not in a valid GameDescriptor schema", fileName));
            throw new XmlNotValidException(validationResult);
        }

        catch (SAXException e) {
            ValidationResult validationResult = new ValidationResult();
            validationResult.add(String.format("file %1$s xml is not in a valid GameDescriptor schema", fileName));
            throw new XmlNotValidException(validationResult);
        }

    }


    public boolean loadDataFromJaxbToGame(GameDescriptor loadedGame) {
        boolean loadSuccess = true;
        String gameType = loadedGame.getGameType();

          if(gameType.equals(String.valueOf(eGameType.Basic))) {
              gameLogic = new BasicGame();
          }
          else if(gameType.equals(String.valueOf(eGameType.Advance))) {
              gameLogic = new AdvancedGame();
          }
          else if(gameType.equals(String.valueOf(eGameType.AdvanceDynamic))) {
              //gameLogic = new DynamicAdvancedGame();
          }
          else {
              loadSuccess = false;
          }
          if(loadSuccess) {

              gameLogic.setGameType(eGameType.valueOf(gameType));
              jaxb.schema.generated.Board loadedBoard = loadedGame.getBoard();
              gameLogic.setBoard(loadedBoard);
              if(!gameType.equals(String.valueOf(eGameType.Basic)))
              {
                  jaxb.schema.generated.Players players = loadedGame.getPlayers();
                  gameLogic.setPlayers(players);
              }
          }

          return loadSuccess;
    }

    public boolean LoadGameFromXmlAndValidate(String filePath) throws XmlNotValidException
    {
        boolean  isValidXML = false;
        //String inValidXML = "Invalid XML File, please load valid xml file !\n ";
        // explicitSquares.clear();
        try{
            loadedGame = loadGameFromFile(filePath);
        }
        catch (XmlNotValidException ex)
        {
            throw new XmlNotValidException(ex.getValidationResult());
        }

        if(loadedGame!= null) {
            isValidXML = gameLogic.checkXMLData(loadedGame);
        }
        if (!isValidXML) {
            throw new XmlNotValidException(new ValidationResult());
        }
        else {
            loadDataFromJaxbToGame(loadedGame); //setBoard in Basic
        }

        return isValidXML;
    }



    }




//    private void runGame()
//    {
//        UserInterface.PrintFirstMenu();
//        int userChoise = UserInterface.GetUserInput(1, 3);
//
//        while(userChoise != EXIT_GAME)
//        {
//            if(userChoise == LOAD_GAME)
//            {
//                isLoadedGame=basicGame.LoadGameFromXmlAndValidate();
//                gameRound = 0;
//                if(isLoadedGame)
//                {
//                    UserInterface.PrintBoard(basicGame.getGameBoard().toString());
//                }
//            }
//            else if(userChoise == START_GAME)
//            {
//                if(isLoadedGame)
//                {
//                    initGame();
//
//                }
//                else
//                {
//                    UserInterface.PrintUserMessage("Cannot start game. You need to load game XML file first!");
//                }
//            }
//
//            UserInterface.PrintFirstMenu();
//            userChoise = UserInterface.GetUserInput(1, 3);
//        }
//            exitGame();
//    }
//
//
//    private void initGame()
//    {
//        if(gameRound >= 1)
//        {
//            basicGame.loadDataFromJaxbToGame(basicGame.getLoadedGame());
//        }
//        basicGame.setBasicPlayers();
//        basicGame.isEndOfGame = false;
//        basicGame.setStartTime(System.currentTimeMillis());
//        UserInterface.PrintBoard(basicGame.getGameBoard().toString());
//        gameLoop();
//    }
//
//
//    private void gameLoop() {
//
//        int option;
//
//        basicGame.setCurrentPlayer(basicGame.getRowPlayer());
//        if (!basicGame.InitMoveCheck())
//        {
//            return;
//
//        }
//        else {
//            UserInterface.PrintUserMessage("Lets Start the Game ...\n Choose an option from the menu below :");
//
//            while (!basicGame.isEndOfGame) {
//                if (basicGame.getCurrentPlayer().getPlayerType() == ePlayerType.COMPUTER) {
//                    basicGame.makeMove();
//                    basicGame.switchPlayer();
//                } else { //HUMAN TURN
//                    UserInterface.PrintSecondMenu();
//                    option = UserInterface.GetUserInput(SHOW_BOARD_AND_CURRENT_PLAYER, LEAVE_GAME);
//
//                    switch (option) {
//                        case SHOW_BOARD_AND_CURRENT_PLAYER:
//                            UserInterface.PrintBoard(basicGame.getGameBoard().toString());
//                            UserInterface.PrintCurrentPlayer(basicGame.getCurrentPlayer().getTurn());
//                            break;
//                        case MAKE_A_MOVE:
//                            basicGame.makeMove();
//                            basicGame.switchPlayer();
//                            break;
//                        case SHOW_STATISTICS:
//                            UserInterface.ShowStatistics(basicGame.getRowPlayer().getNumOfMoves() + basicGame.getColPlayer().getNumOfMoves(), basicGame.TotalGameTime(), basicGame.getRowPlayer().getScore(), basicGame.getColPlayer().getScore());
//                            break;
//                        case LEAVE_GAME: //go To main Menu
//                            basicGame.gameOver();
//                            basicGame.isEndOfGame = true;
//                            break;
//
//                    }
//                }
//            }
//
//            setEndOfGame();
//        }
//    }
//
//   private void exitGame()
//   {
//     UserInterface.exitGameFromMainMenu();
//   }
//
//   private void setEndOfGame()
//   {
//       gameRound++;
//       runGame();
//   }
//




