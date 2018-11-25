package fall2018.csc2017.slidingtiles.UltimateTTT;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

class UltTTTBackendInit {
    String winner;
    String globalWinner;
    int currentActiveBlock = Integer.MAX_VALUE; //In Board?
    int nextActiveBlock = Integer.MAX_VALUE;
    boolean[] disabledBlocks = new boolean[9];
    int score[];
    int currentCell;
    StringBuilder usedCells;
    String resetCells;
    String buttonPressed;
    boolean isP1Turn;       //true for player1. false for player2
    String resetBlockColor;

    int boardStatus[][][];
    int no_terms[];
    ArrayList<String> result;

    Stack<JSONObject> history;

    UltTTTBackendInit() {
        initialize();
    }

    void initialize() {
        winner = "None";
        globalWinner = "None";
        score = new int[2];
        score[0] = 0;           //Score of player 1
        score[1] = 0;           //Score of player 2
        currentCell = Integer.MAX_VALUE;            //initial value
        usedCells = new StringBuilder("");
        resetCells = "All";
        buttonPressed = null;
        isP1Turn = true;
        resetBlockColor = "None"; //TODO: is this like tile?

        boardStatus = new int[9][3][3]; //Making ultTTTBoard
        no_terms = new int[9];
        result = new ArrayList<>();
        history = new Stack<>();

        //put in UltTTTboard
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 3; j++)
                for (int k = 0; k < 3; k++)
                    boardStatus[i][j][k] = -1;

        Arrays.fill(no_terms, 0);
        Arrays.fill(disabledBlocks, false);

    }
}
