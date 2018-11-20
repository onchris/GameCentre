package fall2018.csc2017.slidingtiles.UltimateTTT;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class UltimateTTTBackend {
//Adapted from: https://github.com/Prakash2403/UltimateTicTacToe/blob/master/app/src/main/java/com/example/prakash/ultimatetictactoe/backend/Backend.java


    private String winner;
    private String globalWinner;
    private int currentActiveBlock = Integer.MAX_VALUE; //In Board?
    private int nextActiveBlock = Integer.MAX_VALUE;
    private boolean[] disabledBlocks = new boolean[9];
    private int score[];
    private int currentCell;
    private StringBuilder usedCells;
    private String resetCells;
    private String buttonPressed;
    private boolean isP1Turn;       //true for player1. false for player2
    private String resetBlockColor;

    private int boardStatus[][][];
    private int no_terms[];
    private ArrayList<String> result;

    private Stack<JSONObject> history;

    public UltimateTTTBackend() {
        initialize();
    }

    private void initialize() {
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

    private void updateScore(int num) {
        ++score[num];
    }

    private void updateTurn() {
        isP1Turn = !isP1Turn;
    }

    public JSONObject execute(int cell_number) {
        int row, column;
        JSONObject curr_state;

        resetCells = new String();
        currentActiveBlock = getBlockNumber(cell_number);
        buttonPressed = getButtonPressed(cell_number);
        if (buttonPressed.equals("Reset")) {
            executeReset();
            buttonPressed = "Reset";
            curr_state = toJson();
            updateHistory(curr_state);
            return curr_state;
        } else if (buttonPressed.equals("Undo")) {
            executeUndo();
            buttonPressed = "Undo";
            return toJson();
        }
        currentCell = cell_number;
        row = getRowNumber(cell_number);
        column = getColumnNumber(cell_number);

        usedCells.append(cell_number + "::::");
        updateBoard(currentActiveBlock, row, column);
        no_terms[currentActiveBlock]++;

        winner = getWinner(currentActiveBlock);

        switch (winner) {
            case "Player 1":
                disabledBlocks[currentActiveBlock] = true;
                updateScore(0);
                break;
            case "Player 2":
                disabledBlocks[currentActiveBlock] = true;
                updateScore(1);
                break;
        }
        nextActiveBlock = getNextActiveBlock(cell_number);
        if (!winner.equals("None"))
            result.add(winner);

        globalWinner = findGlobalWinner();
        curr_state = toJson();
        updateTurn();
        updateHistory(curr_state);
        return curr_state;
    }

    private void updateHistory(JSONObject move_json) {
        history.push(move_json);
    }

    private String getButtonPressed(int cell_number) {
        if (cell_number >= 0 && cell_number <= 80)
            return "GameButton";
        else if (cell_number == 100)
            return "Reset";
        else if (cell_number == 200)
            return "Undo";
        return "None";
    }

    private int getColumnNumber(int cell_number) {
        return (cell_number % 9) % 3;
    }

    private int getRowNumber(int cell_number) {
        return (cell_number % 9) / 3;
    }

    private void updateBoard(int blockNumber, int row, int column) {
        if (blockNumber >= 0 && blockNumber <= 8)
            if (isP1Turn)
                boardStatus[blockNumber][row][column] = 0;
            else
                boardStatus[blockNumber][row][column] = 1;
    }

    private String findGlobalWinner() {
        int no_drawn = findOccurrences("Drawn", result);
        int no_win_p1 = findOccurrences("Player 1", result);
        int no_win_p2 = findOccurrences("Player 2", result);
        int deciding_factor = (9 - no_drawn) / 2;

        if (no_win_p1 > deciding_factor)
            return "Player 1";
        else if (no_win_p2 > deciding_factor)
            return "Player 2";
        else if (result.size() == 9 && no_win_p1 == no_win_p2)
            return "Drawn";
        else
            return "None";
    }

    private int findOccurrences(String str, ArrayList list) {
        String curr_item;
        int num_occurrences;
        Iterator itr;
        num_occurrences = 0;
        itr = list.iterator();
        while (itr.hasNext()) {
            curr_item = (String) itr.next();
            if (curr_item.equals(str))
                num_occurrences++;
        }
        return num_occurrences;
    }

    private int getBlockNumber(int cell_number) {
        return cell_number / 9;
    }

    private void executeReset() {
        initialize();
    }

    private void executeUndo() {
        Map previous_values;
        Map current_values;
        JSONObject previous_move;
        JSONObject current_move;
        String used_cells_raw_string_array[];
        String pre_nab;
        if (history.isEmpty()) {
            initialize();
            return;
        }
        current_move = history.pop();

        if (history.isEmpty()) {
            initialize();
            return;
        }

        isP1Turn = !isP1Turn;
        previous_move = history.peek();
        current_values = UltimateTTTInfoManager.parseJson(current_move);
        previous_values = UltimateTTTInfoManager.parseJson(previous_move);
        nextActiveBlock = Integer.parseInt((String) previous_values.get("NextActiveBlock"));
        currentActiveBlock = Integer.parseInt((String) previous_values.get("CurrentActiveBlock"));
        winner = (String) current_values.get("CurrentWinner");
        pre_nab = (String) current_values.get("CurrentActiveBlock");
        resetCells = (String) current_values.get("CurrentCell");
        resetBlockColor = (String) current_values.get("NextActiveBlock");
        used_cells_raw_string_array = usedCells.toString().split("::::");
        usedCells = new StringBuilder();
        for (int i = 0; i < used_cells_raw_string_array.length; i++)
            if (!used_cells_raw_string_array[i].equals(resetCells))
                usedCells.append(used_cells_raw_string_array[i] + "::::");
        score[0] = Integer.parseInt((String) previous_values.get("ScoreP1"));
        score[1] = Integer.parseInt((String) previous_values.get("ScoreP2"));

        // boardstatus is a 3D array.
        System.out.println("Print it" + pre_nab);
        boardStatus[Integer.parseInt(pre_nab)]
                [getRowNumber(Integer.parseInt(resetCells))]
                [getColumnNumber(Integer.parseInt(resetCells))] = -1;
        if (!winner.equals("None")) {
            disabledBlocks[Integer.parseInt(pre_nab)] = false;
            result.remove(0);
        }
    }

    private void print(int[][][] boardStatus) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    System.out.print(boardStatus[i][j][k] + "\t");
                }
                System.out.println();
            }
            System.out.println("\n");
        }
    }

    private JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CurrentActiveBlock", currentActiveBlock);
            jsonObject.put("CurrentCell", currentCell);
            jsonObject.put("NextActiveBlock", nextActiveBlock);
            jsonObject.put("CurrentWinner", winner);
            jsonObject.put("GlobalWinner", globalWinner);
            jsonObject.put("Turn", getTurn());
            jsonObject.put("DisableBlock", getDisableBlockString());
            jsonObject.put("DisableList", usedCells);
            jsonObject.put("ResetList", resetCells);
            jsonObject.put("ScoreP1", score[0]);
            jsonObject.put("ScoreP2", score[1]);
            jsonObject.put("ResetBlockColor", resetBlockColor);
            jsonObject.put("ButtonPressed", buttonPressed);
        } catch (JSONException jsonException) {
            System.out.println("Exception in converting to JSON");
        }
        return jsonObject;
    }

    private String getDisableBlockString() {
        String result = "";
        for (int i = 0; i < 9; i++)
            if (disabledBlocks[i]) {
                result = result + i;
            }
        return result;
    }

    private int getNextActiveBlock(int cell_number) {
        int nab = cell_number % 9;
        if (disabledBlocks[nab])
            return Integer.MAX_VALUE;
        return no_terms[cell_number / 9] == 9 ? Integer.MAX_VALUE : nab;
    }

    private String getWinner(int block_num) {
        for (int i = 0; i < 3; i++) {
            if (boardStatus[block_num][i][0] == boardStatus[block_num][i][1] &&
                    boardStatus[block_num][i][1] == boardStatus[block_num][i][2]) {
                if (!(boardStatus[block_num][i][0] == -1))
                    return getPlayerID(boardStatus[block_num][i][0]);
            } else if (boardStatus[block_num][0][i] == boardStatus[block_num][1][i] &&
                    boardStatus[block_num][1][i] == boardStatus[block_num][2][i]) {
                if (!(boardStatus[block_num][0][i] == -1))
                    return getPlayerID(boardStatus[block_num][0][i]);
            } else if (boardStatus[block_num][0][0] == boardStatus[block_num][1][1] &&
                    boardStatus[block_num][1][1] == boardStatus[block_num][2][2]) {
                if (!(boardStatus[block_num][0][0] == -1))
                    return getPlayerID(boardStatus[block_num][1][1]);
            } else if (boardStatus[block_num][0][2] == boardStatus[block_num][1][1] &&
                    boardStatus[block_num][1][1] == boardStatus[block_num][2][0]) {
                if (!(boardStatus[block_num][0][2] == -1))
                    return getPlayerID(boardStatus[block_num][1][1]);
            }
        }
        if (no_terms[block_num] == 9)
            return "Drawn";
        return "None";
    }

    private String getPlayerID(int num) {
        if (num == 0)
            return "Player 1";
        return "Player 2";
    }

    private String getTurn() {
        if (isP1Turn)
            return "Player 1";
        return "Player 2";
    }
}

