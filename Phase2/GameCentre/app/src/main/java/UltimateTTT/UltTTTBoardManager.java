package UltimateTTT;

import java.util.Map;

//Adapted from: https://github.com/Prakash2403/UltimateTicTacToe/blob/master/app/src/main/java/com/example/prakash/ultimatetictactoe/frontend/Fifth.java
public class UltTTTBoardManager {
    String winner;
    String globalWinner;

    int currentActiveBlock;
    int nextActiveBlock;
    String disableBlock;

    int scoreP1;
    int scoreP2;

    int currentCell;
    String usedCells[];
    String resetCells;

    String isP1Turn;
    String buttonPressed;
    String resetBlockColor;

    UltTTTBoardManager(Map result) {
        winner = (String) result.get("CurrentWinner");
        globalWinner = (String) result.get("GlobalWinner");

        currentActiveBlock = Integer.parseInt((String) result.get("CurrentActiveBlock"));
        nextActiveBlock = Integer.parseInt((String) result.get("NextActiveBlock"));
        disableBlock = (String) result.get("DisableBlock");

        currentCell = Integer.parseInt((String) result.get("CurrentCell"));
        usedCells = ((String) result.get("DisableList")).split("::::");
        resetCells = (String) result.get("ResetList");

        scoreP1 = Integer.parseInt((String) result.get("ScoreP1"));
        scoreP2 = Integer.parseInt((String) result.get("ScoreP2"));

        buttonPressed = (String) result.get("ButtonPressed");
        isP1Turn = (String) result.get("Turn");
        resetBlockColor = (String) result.get("ResetBlockColor");
    }
}