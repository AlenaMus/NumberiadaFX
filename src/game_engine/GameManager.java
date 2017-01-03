package game_engine;

//import javax.xml.bind.Marshaller;


public class GameManager  { //implements IGameManager

    private static final int LOAD_GAME = 1;
    private static final int START_GAME = 2;
    private static final int EXIT_GAME = 3;

    private static final int SHOW_BOARD_AND_CURRENT_PLAYER = 1;
    private static final int MAKE_A_MOVE = 2;
    private static final int SHOW_STATISTICS = 3;
    private static final int LEAVE_GAME = 4;
    public static final int BAD_SQUARE = 100;

    private static int gameRound = 0;
    private boolean isLoadedGame = false;
   // protected eGameType gameType;
    private BasicGame basicGame;




    public GameManager()
    {
        basicGame = new BasicGame();
        basicGame.setLoadedGame(null);
       // runGame();
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



}
