package fall2018.csc2017.slidingtiles.UltimateTTT;

class UltimateTTTBackend {
//Adapted from: https://github.com/Prakash2403/UltimateTicTacToe/blob/master/app/src/main/java/com/example/prakash/ultimatetictactoe/backend/Backend.java

    UltTTTBackendExecute executer;

    UltimateTTTBackend() {
        UltTTTBackendInit initializer = new UltTTTBackendInit();
        initializer.initialize();

        UltTTTGameStateScanner scanner = new UltTTTGameStateScanner(initializer);
        UltTTTGameStates gamestates = new UltTTTGameStates(initializer, scanner);
        executer = new UltTTTBackendExecute(initializer, gamestates);
    }
}

