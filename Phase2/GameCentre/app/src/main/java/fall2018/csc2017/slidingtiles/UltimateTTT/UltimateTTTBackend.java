package fall2018.csc2017.slidingtiles.UltimateTTT;

//Adapted from: https://github.com/Prakash2403/UltimateTicTacToe/blob/master/app/src/main/java/com/example/prakash/ultimatetictactoe/backend/Backend.java

/**
 * Class for Ultimate Tic Tac Toe backend.
 */
public class UltimateTTTBackend {

    /**
     * The backend executer for ultimate tic tac toe game
     */
    public UltTTTBackendExecute executer;
    /**
     * The activity for ultimate tic tac toe game
     */
    UltimateTTTGameActivity activity;
    /**
     * The game state scanner for ultimate tic tac toe game
     */
    UltTTTGameStateScanner scanner;

    /**
     * The backend for ultimate tic tac toe game
     *
     * @param activity the activity for ultimate tic tac toe game
     */
    public UltimateTTTBackend(UltimateTTTGameActivity activity) {
        UltTTTBackendInit initializer = new UltTTTBackendInit();
        initializer.initialize();

        scanner = new UltTTTGameStateScanner(initializer, activity);
        UltTTTGameStates gamestates = new UltTTTGameStates(initializer, scanner);
        executer = new UltTTTBackendExecute(initializer, gamestates);
        this.activity = activity;
    }
}

