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

public class GameManager {
    
    public static int gameRound = 0;
    private boolean isLoadedGame = false;
    protected eGameType gameType;
    private GameLogic gameLogic = null;
    private GameDescriptor loadedGame =null;

    public GameLogic getGameLogic(){return gameLogic;}


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



    public void LoadGameFromXmlAndValidate(String filePath) throws XmlNotValidException
    {
        String gameType ="";

        try{
              loadedGame = loadGameFromFile(filePath);
        }
        catch (XmlNotValidException ex)
        {
            throw new XmlNotValidException(ex.getValidationResult());
        }

        if(loadedGame!= null) {

            gameType = loadedGame.getGameType();

            if(gameRound == 0) {

                if (gameType.equals(String.valueOf(eGameType.Basic))) {
                    gameLogic = new BasicGame();
                } else if (gameType.equals(String.valueOf(eGameType.Advance))) {
                    gameLogic = new AdvancedGame();
                } else if (gameType.equals(String.valueOf(eGameType.AdvanceDynamic))) {
                    //gameLogic = new DynamicAdvancedGame();
                } else {
                    ValidationResult valid = new ValidationResult();
                    valid.add("Invalid Game Type");
                    throw new XmlNotValidException(valid);
                }
            }
            gameLogic.initGame();
            gameLogic.setGameType(eGameType.valueOf(gameType));
            gameLogic.checkXMLData(loadedGame);
        }

          gameLogic.loadDataFromJaxbToGame(loadedGame,gameType);
    }


}




