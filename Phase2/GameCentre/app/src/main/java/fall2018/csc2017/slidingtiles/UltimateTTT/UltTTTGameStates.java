package fall2018.csc2017.slidingtiles.UltimateTTT;

//Adapted from: https://github.com/Prakash2403/UltimateTicTacToe/blob/master/app/src/main/java/com/example/prakash/ultimatetictactoe/backend/Backend.java

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class for Ultimate Tic Tac Toe game states.
 */
class UltTTTGameStates {
    /**
     * The initializer for ultimate tic tac toe game
     */
    private UltTTTBackendInit initializer;
    /**
     * The scanner for ultimate tic tac toe game
     */
    private UltTTTGameStateScanner scanner;

    /**
     * Game states for Ultimate TTT
     *
     * @param initializer the initializer for ultimate tic tac toe game
     * @param scanner     the scanner for ultimate tic tac toe game
     */
    UltTTTGameStates(UltTTTBackendInit initializer, UltTTTGameStateScanner scanner) {
        this.initializer = initializer;
        this.scanner = scanner;
    }

    /**
     * Convert information to jsonObject
     *
     * @return JSONObject
     */
    JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CurrentActiveBlock", initializer.currentActiveBlock);
            jsonObject.put("CurrentCell", initializer.currentCell);
            jsonObject.put("NextActiveBlock", initializer.nextActiveBlock);
            jsonObject.put("CurrentWinner", initializer.winner);
            jsonObject.put("GlobalWinner", initializer.globalWinner);
            jsonObject.put("Turn", getTurn());
            jsonObject.put("DisableBlock", getDisableBlockString());
            jsonObject.put("DisableList", initializer.usedCells);
            jsonObject.put("ResetList", initializer.resetCells);
            jsonObject.put("ScoreP1", initializer.score[0]);
            jsonObject.put("ScoreP2", initializer.score[1]);
            jsonObject.put("ResetBlockColor", initializer.resetBlockColor);
            jsonObject.put("ButtonPressed", initializer.buttonPressed);
        } catch (JSONException jsonException) {
            System.out.println("Exception in converting to JSON");
        }
        return jsonObject;
    }

    /**
     * Add moves to history
     *
     * @param move_json the move just taken
     */
    void updateHistory(JSONObject move_json) {
        initializer.history.push(move_json);
    }

    /**
     * Update the board
     *
     * @param blockNumber the block number
     * @param row         the row number
     * @param column      the column number
     */
    void updateBoard(int blockNumber, int row, int column) {
        if (blockNumber >= 0 && blockNumber <= 8)
            if (initializer.isP1Turn)
                initializer.boardStatus[blockNumber][row][column] = 0;
            else
                initializer.boardStatus[blockNumber][row][column] = 1;
    }

    /**
     * Update the score
     *
     * @param num the score
     */
    void updateScore(int num) {
        ++initializer.score[num];
    }

    /**
     * Update the turn
     */
    void updateTurn() {
        initializer.isP1Turn = !initializer.isP1Turn;
    }

    /**
     * Gets the winner
     *
     * @param block_num block number
     * @return the winner or null depends on different situations
     */
    String getWinner(int block_num) {
        for (int i = 0; i < 3; i++) {
            if (initializer.boardStatus[block_num][i][0] == initializer.boardStatus[block_num][i][1] &&
                    initializer.boardStatus[block_num][i][1] == initializer.boardStatus[block_num][i][2]) {
                if (!(initializer.boardStatus[block_num][i][0] == -1))
                    return getPlayerID(initializer.boardStatus[block_num][i][0]);
            } else if (initializer.boardStatus[block_num][0][i] == initializer.boardStatus[block_num][1][i] &&
                    initializer.boardStatus[block_num][1][i] == initializer.boardStatus[block_num][2][i]) {
                if (!(initializer.boardStatus[block_num][0][i] == -1))
                    return getPlayerID(initializer.boardStatus[block_num][0][i]);
            } else if (initializer.boardStatus[block_num][0][0] == initializer.boardStatus[block_num][1][1] &&
                    initializer.boardStatus[block_num][1][1] == initializer.boardStatus[block_num][2][2]) {
                if (!(initializer.boardStatus[block_num][0][0] == -1))
                    return getPlayerID(initializer.boardStatus[block_num][1][1]);
            } else if (initializer.boardStatus[block_num][0][2] == initializer.boardStatus[block_num][1][1] &&
                    initializer.boardStatus[block_num][1][1] == initializer.boardStatus[block_num][2][0]) {
                if (!(initializer.boardStatus[block_num][0][2] == -1))
                    return getPlayerID(initializer.boardStatus[block_num][1][1]);
            }
        }
        if (initializer.no_terms[block_num] == 9)
            return "Drawn";
        return "None";
    }

    /**
     * Gets which player is playing next
     *
     * @return which player is playing next
     */
    private String getTurn() {
        if (initializer.isP1Turn)
            return "Player 1";
        return "Player 2";
    }

    /**
     * Gets the next active block
     *
     * @param cell_number the cell number
     * @return the next active block
     */
    int getNextActiveBlock(int cell_number) {
        int nab = cell_number % 9;
        if (initializer.disabledBlocks[nab])
            return Integer.MAX_VALUE;
        return initializer.no_terms[cell_number / 9] == 9 ? Integer.MAX_VALUE : nab;
    }

    /**
     * Gets string format of disable blocks
     *
     * @return the string format of disable blocks
     */
    private String getDisableBlockString() {
        String result = "";
        for (int i = 0; i < 9; i++)
            if (initializer.disabledBlocks[i]) {
                result = result + i;
            }
        return result;
    }

    /**
     * Gets the player's id
     *
     * @param num the id number
     * @return the player corresponding to the id
     */
    private String getPlayerID(int num) {
        if (num == 0)
            return "Player 1";
        return "Player 2";
    }

    /**
     * Gets the scanner for ultimate ttt game
     *
     * @return the scanner for ultimate ttt game
     */
    UltTTTGameStateScanner getScanner() {
        return scanner;
    }
}
