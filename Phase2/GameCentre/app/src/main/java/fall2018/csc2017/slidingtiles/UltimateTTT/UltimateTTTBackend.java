package fall2018.csc2017.slidingtiles.UltimateTTT;

class UltimateTTTBackend {
//Adapted from: https://github.com/Prakash2403/UltimateTicTacToe/blob/master/app/src/main/java/com/example/prakash/ultimatetictactoe/backend/Backend.java

    /**
     * The backend executer for ultimate tic tac toe game
     */
    UltTTTBackendExecute executer;
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
    UltimateTTTBackend(UltimateTTTGameActivity activity) {
        UltTTTBackendInit initializer = new UltTTTBackendInit();
        initializer.initialize();

        scanner = new UltTTTGameStateScanner(initializer, activity);
        UltTTTGameStates gamestates = new UltTTTGameStates(initializer, scanner);
        executer = new UltTTTBackendExecute(initializer, gamestates);
        this.activity = activity;
    }
}

