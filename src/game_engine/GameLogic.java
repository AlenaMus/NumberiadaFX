package game_engine;

import game_objects.*;

import game_validation.ValidationResult;
import game_validation.XmlNotValidException;
import jaxb.schema.generated.GameDescriptor;
import jaxb.schema.generated.Range;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class GameLogic {

    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 6;

    public static final int HUMAN_PLAYER = 1;
    public static final int COMPUTER_PLAYER =2;
    public static final int COMPUTERS_GAME =3;

    protected String gameFile = " ";
    public  boolean isEndOfGame = false;

    //protected List<Player> players = new ArrayList<Player>();
    protected Map<Integer, game_objects.Player> players = null;

    protected GameDescriptor loadedGame;
    protected  int numOfPlayers;
    protected game_objects.Board gameBoard;
    protected game_objects.Player currentPlayer;


    public abstract void makeMove();
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public void setCurrentPlayer(game_objects.Player currentPlayer)
    {
        this.currentPlayer = currentPlayer;
    }
    public GameDescriptor getLoadedGame() {
        return loadedGame;
    }
    public void setLoadedGame(GameDescriptor loadedGame) {
        this.loadedGame = loadedGame;
    }
    public int getNumOfPlayers ()
    {
        return numOfPlayers;
    }
    public void setNumOfPlayers(int num) { numOfPlayers = num; }
    public abstract Map<Integer, Player>  getPlayers();


    public abstract boolean InitMoveCheck();
    public abstract boolean checkXMLData(GameDescriptor loadedGame);
    public abstract boolean checkRandomBoardValidity(Range boardRange, int boardSize);
    public abstract boolean checkExplicitBoard(Range range, List<jaxb.schema.generated.Square> squares, jaxb.schema.generated.Marker marker, int boardSize);
    public abstract boolean isInBoardRange(int num, int size);
    public abstract void gameOver();


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

    public void loadDataFromJaxbToGame(GameDescriptor loadedGame)
    {
         String gameType = loadedGame.getGameType();
        jaxb.schema.generated.Board loadedBoard = loadedGame.getBoard();
        setBoard(loadedBoard);
        jaxb.schema.generated.Players players = loadedGame.getPlayers();
        setPlayers(players);


//         switch (eGameType.valueOf(gameType))
//         {
//         case Basic: setBasicPlayers();
//                      break;
//         case Advance: Players players = loadedGame.getPlayers();
//                            setPlayers(players);
//                             break;
//          case AdvanceDynamic: DynamicPlayers dynamicPlayers = loadedGame.getDynamicPlayers();
//                             //setDynamicPlayers(dynamicPlayers);
//                                break;
//          }

    }

    public void setPlayers(jaxb.schema.generated.Players gamePlayers)
    {
       players = new HashMap<>(numOfPlayers);
        for (jaxb.schema.generated.Player player:gamePlayers.getPlayer()) {
            int id = player.getId().intValue();
            String name = player.getName();
            ePlayerType playerType = ePlayerType.valueOf(player.getType());
            int color = player.getColor();
            game_objects.Player player1 = new game_objects.Player(playerType,name,id,color);
            players.put(id,player1);
        }
    }

    public boolean LoadGameFromXmlAndValidate(String filePath) throws XmlNotValidException
    {
        boolean  isValidXML = false;
        String inValidXML = "Invalid XML File, please load valid xml file !\n ";
        // explicitSquares.clear();
        // UserInterface.ValidationErrors.clear();

        // while (!isValidXML)
        // {
        // filePath = UserInterface.getXMLfile();
        try{
            loadedGame = loadGameFromFile(filePath);
        }
        catch (XmlNotValidException ex)
        {
            throw new XmlNotValidException(ex.getValidationResult());
        }

        if(loadedGame!=null) {
            isValidXML = checkXMLData(loadedGame);
        }
        if (!isValidXML) {
            //  if(!UserInterface.ValidationErrors.contains(inValidXML)) {
            //    UserInterface.ValidationErrors.add(inValidXML);
            // }
            // UserInterface.PrintValidationErrors();
        }
        else {
            loadDataFromJaxbToGame(loadedGame); //setBoard in Basic
            //  UserInterface.PrintUserMessage("The XML Game file Loaded Successfully");
        }

        //}

        return isValidXML;
    }


    public void setBoard(jaxb.schema.generated.Board board){}


    protected int updateBoard(Point squareLocation) //implement in Board - returns updated value of row/column
    {
        int squareValue = gameBoard.updateBoard(squareLocation);
        return squareValue;
    }


    protected void updateUserData(int squareValue) // in Player?
    {
        currentPlayer.setNumOfMoves(currentPlayer.getNumOfMoves()+1); //maybe do totalmoves var in gameManager
        currentPlayer.setScore(squareValue);
    }

    protected void switchPlayer(){}

    public Board getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(Board gameBoard) {
        this.gameBoard = gameBoard;
    }



    protected boolean checkBoardXML(GameDescriptor loadedGame)
    {
        boolean isValidBoard;
        int size;
        eBoardType boardType;
        List<jaxb.schema.generated.Square> squares;

        jaxb.schema.generated.Board gameBoard = loadedGame.getBoard();
        jaxb.schema.generated.Structure structure = gameBoard.getStructure();

        size = gameBoard.getSize().intValue();
        if((size >= Board.MIN_SIZE )&& (size <= Board.MAX_SIZE))
        {
            boardType = eBoardType.valueOf(structure.getType());

            if(boardType.equals(eBoardType.Explicit))
            {
                 squares = structure.getSquares().getSquare();
                isValidBoard = checkExplicitBoard(structure.getRange(),squares,structure.getSquares().getMarker(),size);
            }
            else
            {
                if(boardType.equals(eBoardType.Random))
                {
                    isValidBoard =checkRandomBoardValidity(structure.getRange(),size);

                }
                else
                {
                    isValidBoard = false;
                    //   UserInterface.ValidationErrors.add(String.format("Board type validation error : %s doesn't exist!",boardType));
                }
            }
        }
        else
        {
            isValidBoard = false;
            //   UserInterface.ValidationErrors.add(String.format("Board size validation error : %d is not in board range size %d - %d!",size, Board.MIN_SIZE, Board.MAX_SIZE));
        }

        return isValidBoard;
    }

    protected boolean checkAndSetPlayersXML()
    {
        loadedGame.getPlayers();
        return true;

    }


}
