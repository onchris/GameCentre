package fall2018.csc2017.slidingtiles.UltimateTTT;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

class UltTTTBackendInit {
    /**
     * The winner of current round
     */
    String winner;
    /**
     * The winner of the game
     */
    String globalWinner;
    /**
     * The current active block
     */
    int currentActiveBlock = Integer.MAX_VALUE; //In Board?
    /**
     * The next active block
     */
    int nextActiveBlock = Integer.MAX_VALUE;
    /**
     * The list of disable blocks
     */
    boolean[] disabledBlocks = new boolean[9];
    /**
     * The list of scores
     */
    int score[];
    /**
     * The current cell
     */
    int currentCell;
    /**
     * The used cells
     */
    StringBuilder usedCells;
    /**
     * The cells to be reset
     */
    String resetCells;
    /**
     * The button that is pressed
     */
    String buttonPressed;
    /**
     * A check if it is player1's turn
     */
    boolean isP1Turn;       //true for player1. false for player2
    /**
     * The block color to reset
     */
    String resetBlockColor;
    /**
     * The status of the board
     */
    int boardStatus[][][];
    /**
     * No terms
     */
    int no_terms[];

    /**
     * The list of results of each round
     */
    ArrayList<String> result;

    /**
     * The history of user's moves
     */
    Stack<JSONObject> history;

    /**
     * Initialize Ultimate Tic Tac Toe game
     */
    UltTTTBackendInit() {
        initialize();
    }

    /**
     * Initialize Ultimate Tic Tac Toe game
     */
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
