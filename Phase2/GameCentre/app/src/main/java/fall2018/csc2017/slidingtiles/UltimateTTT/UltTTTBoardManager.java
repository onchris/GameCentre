package fall2018.csc2017.slidingtiles.UltimateTTT;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import fall2018.csc2017.slidingtiles.R;
import fall2018.csc2017.slidingtiles.ScoreBoard;

//Adapted from: https://github.com/Prakash2403/UltimateTicTacToe/blob/master/app/src/main/java/com/example/prakash/ultimatetictactoe/frontend/Fifth.java
public class UltTTTBoardManager {
    /**
     * The winner of current round
     */
    private String winner;
    /**
     * The winner of the game
     */
    private String globalWinner;
    /**
     * The current active block
     */
    private int currentActiveBlock;
    /**
     * The next active block
     */
    private int nextActiveBlock;
    /**
     * The disable block
     */
    private String disableBlock;
    /**
     * The score of player1
     */
    private int scoreP1;
    /**
     * The score of player2
     */
    private int scoreP2;
    /**
     * The current cell
     */
    private int currentCell;
    /**
     * The used cells
     */
    private String usedCells[];
    /**
     * The cells to be reset
     */
    private String resetCells;
    /**
     * A check if it is player1's turn
     */
    private String isP1Turn;
    /**
     * The button that is pressed
     */
    private String buttonPressed;
    /**
     * The block color to reset
     */
    private String resetBlockColor;
    /**
     * List of image buttons
     */
    private ImageButton[] ImageButtons;
    /**
     * The tables
     */
    private TableLayout tables[];
    /**
     * The connector of Ultimate Tic Tac Toe game
     */
    private UltTTTConnector connector;
    /**
     * The game activity of Ultimate Tic Tac Toe game
     */
    private UltimateTTTGameActivity activity;
    /**
     * The cell manager of Ultimate Tic Tac Toe game
     */
    private UltTTTCellManager cellManager;

    /**
     * Manage the board of Ultimate Tic Tac Toe game
     *
     * @param result    the result
     * @param connector the connector of Ultimate Tic Tac Toe game
     */
    UltTTTBoardManager(Map result, UltTTTConnector connector) {
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

        this.connector = connector;
        ImageButtons = connector.getImageButtons();
        tables = connector.getTables();
        activity = (UltimateTTTGameActivity) connector.getActivity();
        cellManager = new UltTTTCellManager(connector);
    }

    /**
     * Operate the board
     */
    void operate() {
        if (buttonPressed.equals("Reset")) {
            activity.initialize();
            cellManager.enableAll();
            return;
        } else if (buttonPressed.equals("Undo")) {
            if (resetCells.equals("All"))
                activity.initialize();
            else {
                ImageButtons[Integer.parseInt(resetCells)].
                        setBackgroundResource(R.drawable.ult_clearimage);
            }
            if (resetBlockColor.equals("None")) {
                if (Integer.parseInt(resetBlockColor) != Integer.MAX_VALUE)
                    tables[Integer.parseInt(resetBlockColor)].
                            setBackgroundColor(Color.BLACK);
            }
        }
        if (buttonPressed.equals("GameButton")) {
            if (isP1Turn.equals("Player 1")) {
                ImageButtons[currentCell].setBackgroundResource(R.drawable.ult_cross);
            } else {
                ImageButtons[currentCell].setBackgroundResource(R.drawable.ult_o);
            }
        }
        changeTableColor(nextActiveBlock, currentActiveBlock,
                isP1Turn, buttonPressed);
        cellManager.disableAll();
        cellManager.enableBlock(nextActiveBlock);
        cellManager.disableWinnerBlocks(disableBlock);
        cellManager.disableUsedCells(usedCells);
        setText(connector.scoreP1, Integer.toString(scoreP1));
        setText(connector.scoreP2, Integer.toString(scoreP2));

        if (winner.equals("Player 1"))
            Toast.makeText(activity, activity.P1Name + " won this round", Toast.LENGTH_SHORT).show();
        else if (winner.equals("Player 2"))
            Toast.makeText(activity, activity.P2Name + " won this round", Toast.LENGTH_SHORT).show();

        if (!globalWinner.equals("None"))
            gameOver(globalWinner);
    }

    /**
     * Change table's color
     *
     * @param nextActiveBlock    the next active block
     * @param currentActiveBlock the current active block
     * @param player             the current player
     * @param buttonPressed      the button pressed
     */
    private void changeTableColor(int nextActiveBlock, int currentActiveBlock, String player,
                                  String buttonPressed) {
        int color;
        if (player.equals("Player 1"))
            color = Color.GREEN;
        else
            color = Color.RED;

        if (player.equals("Player 1") && buttonPressed.equals("Undo"))
            color = Color.RED;
        else if (player.equals("Player 2") && buttonPressed.equals("Undo"))
            color = Color.GREEN;


        if (currentActiveBlock != Integer.MAX_VALUE)
            tables[currentActiveBlock].setBackgroundColor(Color.BLACK);
        if (nextActiveBlock != Integer.MAX_VALUE)
            tables[nextActiveBlock].setBackgroundColor(color);
    }

    /**
     * Manage the board when game is over
     *
     * @param global_winner the winner of the game
     */
    private void gameOver(String global_winner) {
        cellManager.disableAll();
        Intent tmp = new Intent(this.activity, ScoreBoard.class);
        tmp.putExtra("currentGame","ultTTT");
        if (activity.IS_GUEST) {
            tmp.putExtra("currentUsername", "-1");
        } else{
            tmp.putExtra("currentUsername", activity.P1Name);
        }
        if (global_winner.equals(activity.P1Name)) {
            tmp.putExtra("currentScore", "1");
        } else {
            tmp.putExtra("currentScore", "0");
        } //TODO: save score to account if there is one
        activity.startActivity(tmp);
//        AlertDialog ad = new AlertDialog.Builder(activity)
//                .setTitle("WINNER!!!")
//                .setMessage(connector.backend.scanner.getGlobalWinnerName(global_winner))
//                .setPositiveButton("Restart Game", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //Restart Game
//                        activity.initialize();
//                        cellManager.enableAll();
//                        connector.backend.executer.execute(100);
//                    }
//                })
//                .setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent i = new Intent(activity, ScoreBoard.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        activity.startActivity(i);
//                    }
//                })
//                //.setIcon(R.drawable.trophy)
//                .show();
//        ad.setCanceledOnTouchOutside(false);
    }

    /**
     * Sets the text
     */
    private void setText(TextView tv, String s) {
        tv.setText(s);
    }

    public UltTTTConnector getConnector() {
        return connector;
    }
}