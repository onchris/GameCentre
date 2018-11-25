package fall2018.csc2017.slidingtiles.UltimateTTT;

public class UltimateTTTBackend {
//Adapted from: https://github.com/Prakash2403/UltimateTicTacToe/blob/master/app/src/main/java/com/example/prakash/ultimatetictactoe/backend/Backend.java



    private UltTTTBackendInit initializer;
    UltTTTBackendExecute executer;
    private UltTTTGameStates gamestates;
    private UltTTTGameStateScanner scanner;

    public UltimateTTTBackend() {
        initializer = new UltTTTBackendInit();
        initializer.initialize();

        scanner = new UltTTTGameStateScanner(initializer);
        gamestates = new UltTTTGameStates(initializer, scanner);
        executer = new UltTTTBackendExecute(initializer, gamestates);
    }
}

