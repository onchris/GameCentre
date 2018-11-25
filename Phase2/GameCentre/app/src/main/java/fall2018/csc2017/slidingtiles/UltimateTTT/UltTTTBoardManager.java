package fall2018.csc2017.slidingtiles.UltimateTTT;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.widget.ImageButton;
import android.widget.TableLayout;

import java.util.Map;

import fall2018.csc2017.slidingtiles.R;
import fall2018.csc2017.slidingtiles.ScoreBoard;

//Adapted from: https://github.com/Prakash2403/UltimateTicTacToe/blob/master/app/src/main/java/com/example/prakash/ultimatetictactoe/frontend/Fifth.java
public class UltTTTBoardManager {
    private String winner;
    private String globalWinner;

    private int currentActiveBlock;
    private int nextActiveBlock;
    private String disableBlock;

    private int scoreP1;
    private int scoreP2;

    private int currentCell;
    private String usedCells[];
    private String resetCells;

    private String isP1Turn;
    private String buttonPressed;
    private String resetBlockColor;

    private ImageButton[] ImageButtons;
    private TableLayout tables[];
    private UltTTTConnector connector;
    private UltimateTTTGameActivity activity;


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
        activity = (UltimateTTTGameActivity)connector.getActivity();
    }

    void operate() {
        if (buttonPressed.equals("Reset")) {
            activity.initialize();
            activity.enableAll();
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
        activity.disableAll();
        activity.enableBlock(nextActiveBlock);
        activity.disableWinnerBlocks(disableBlock);
        activity.disableUsedCells(usedCells);
        activity.setText(connector.scoreP1, Integer.toString(scoreP1));
        activity.setText(connector.scoreP2, Integer.toString(scoreP2));

        if (winner.equals("Player 1"))
            activity.showToast(activity.P1Name + " won this round");
        else if (winner.equals("Player 2"))
            activity.showToast(activity.P2Name + " won this round");

        if (!globalWinner.equals("None"))
            gameOver(globalWinner);
    }

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

    private void gameOver(String global_winner) {
        activity.disableAll();
        AlertDialog ad = new AlertDialog.Builder(activity)
                .setTitle("WINNER!!!")
                .setMessage(activity.getGlobalWinnerName(global_winner))
                .setPositiveButton("Restart Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Restart Game
                        activity.initialize();
                        activity.enableAll();
                        connector.backend.execute(100);
                    }
                })
                .setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(activity, ScoreBoard.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(i);
                    }
                })
                //.setIcon(R.drawable.trophy)
                .show();
        ad.setCanceledOnTouchOutside(false);
    }

}