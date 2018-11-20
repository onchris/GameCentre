package fall2018.csc2017.slidingtiles.UltimateTTT;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import fall2018.csc2017.slidingtiles.Account;
import fall2018.csc2017.slidingtiles.GameSelection;
import fall2018.csc2017.slidingtiles.R;
import fall2018.csc2017.slidingtiles.ScoreBoard;

import fall2018.csc2017.slidingtiles.UtilityManager;

public class UltimateTTTGameActivity extends AppCompatActivity implements View.OnClickListener
        //Adapted from: https://github.com/Prakash2403/UltimateTicTacToe/blob/master/app/src/main/java/com/example/prakash/ultimatetictactoe/frontend/Fifth.java
{
    TextView scoreP1;
    TextView scoreP2;
    ImageButton breset;
    ImageButton bundo;
    UltimateTTTBackend backend;
    private ImageButton[] ImageButtons;
    private TableLayout tables[];
    private Account currentAccount;
    private boolean IS_GUEST = false;

    String P1Name;
    String P2Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i;
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_uttt_main);
        i = getIntent();
        bind();
        initialize();
        if (GameSelection.IS_GUEST) {
            IS_GUEST = true;
            P1Name = "Guest1";
            P2Name = "Guest2";
        } else {
            currentAccount = (Account) getIntent().getSerializableExtra("account");
            P1Name = currentAccount.getUsername();
            P2Name = "Guest";
        }
    }

    private void bind() {
        scoreP1 = findViewById(R.id.textView3);
        scoreP2 = findViewById(R.id.textView4);
        backend = new UltimateTTTBackend();
        breset = findViewById(R.id.imageButton4);
        bundo = findViewById(R.id.imageButton3);
        ImageButtons =
                new ImageButton[]{
                        findViewById(R.id.b00), findViewById(R.id.b01),
                        findViewById(R.id.b02), findViewById(R.id.b03),
                        findViewById(R.id.b04), findViewById(R.id.b05),
                        findViewById(R.id.b06), findViewById(R.id.b07),
                        findViewById(R.id.b08), findViewById(R.id.b09),
                        findViewById(R.id.b10), findViewById(R.id.b11),
                        findViewById(R.id.b12), findViewById(R.id.b13),
                        findViewById(R.id.b14), findViewById(R.id.b15),
                        findViewById(R.id.b16), findViewById(R.id.b17),
                        findViewById(R.id.b18), findViewById(R.id.b19),
                        findViewById(R.id.b20), findViewById(R.id.b21),
                        findViewById(R.id.b22), findViewById(R.id.b23),
                        findViewById(R.id.b24), findViewById(R.id.b25),
                        findViewById(R.id.b26), findViewById(R.id.b27),
                        findViewById(R.id.b28), findViewById(R.id.b29),
                        findViewById(R.id.b30), findViewById(R.id.b31),
                        findViewById(R.id.b32), findViewById(R.id.b33),
                        findViewById(R.id.b34), findViewById(R.id.b35),
                        findViewById(R.id.b36), findViewById(R.id.b37),
                        findViewById(R.id.b38), findViewById(R.id.b39),
                        findViewById(R.id.b40), findViewById(R.id.b41),
                        findViewById(R.id.b42), findViewById(R.id.b43),
                        findViewById(R.id.b44), findViewById(R.id.b45),
                        findViewById(R.id.b46), findViewById(R.id.b47),
                        findViewById(R.id.b48), findViewById(R.id.b49),
                        findViewById(R.id.b50), findViewById(R.id.b51),
                        findViewById(R.id.b52), findViewById(R.id.b53),
                        findViewById(R.id.b54), findViewById(R.id.b55),
                        findViewById(R.id.b56), findViewById(R.id.b57),
                        findViewById(R.id.b58), findViewById(R.id.b59),
                        findViewById(R.id.b60), findViewById(R.id.b61),
                        findViewById(R.id.b62), findViewById(R.id.b63),
                        findViewById(R.id.b64), findViewById(R.id.b65),
                        findViewById(R.id.b66), findViewById(R.id.b67),
                        findViewById(R.id.b68), findViewById(R.id.b69),
                        findViewById(R.id.b70), findViewById(R.id.b71),
                        findViewById(R.id.b72), findViewById(R.id.b73),
                        findViewById(R.id.b74), findViewById(R.id.b75),
                        findViewById(R.id.b76), findViewById(R.id.b77),
                        findViewById(R.id.b78), findViewById(R.id.b79),
                        findViewById(R.id.b80)
                };

        tables =
                new TableLayout[]{
                        findViewById(R.id.table0),
                        findViewById(R.id.table1),
                        findViewById(R.id.table2),
                        findViewById(R.id.table3),
                        findViewById(R.id.table4),
                        findViewById(R.id.table5),
                        findViewById(R.id.table6),
                        findViewById(R.id.table7),
                        findViewById(R.id.table8),
                };
    }


    private void initialize() {
        scoreP1.setText("0");
        scoreP2.setText("0");
        for (ImageButton ImageButton : ImageButtons) {
            ImageButton.setOnClickListener(this);
            ImageButton.setBackgroundResource(R.drawable.ult_clearimage);
        }
        for (TableLayout tableLayout : tables)
            tableLayout.setBackgroundColor(Color.BLACK);
        breset.setOnClickListener(this);
        breset.setEnabled(true);
        bundo.setOnClickListener(this);
        bundo.setEnabled(true);
    }

    private void enableAll() {
        for (android.widget.ImageButton ImageButton : ImageButtons) ImageButton.setEnabled(true);
    }

    private void disableAll() {
        for (ImageButton ImageButton : ImageButtons) ImageButton.setEnabled(false);
    }

    private void enable(int id) {
        ImageButtons[id].setEnabled(true);
    }

    private void disable(int id) {
        ImageButtons[id].setEnabled(false);
    }


    @Override
    public void onClick(View v) {
        int index;
        ImageButton curr_button;
        curr_button = findViewById(v.getId());
        index = getIndex(curr_button);
        runFrontEnd(index);
    }

    public void runFrontEnd(int index) {
        JSONObject response;
        response = backend.execute(index);
        UltTTTBoardManager ultTTTBoardManager = new UltTTTBoardManager(UltimateTTTInfoManager.parseJson(response));
        if (!IS_GUEST) {
            UtilityManager.saveUltimateTTTUltTTTBoardManager(this, currentAccount, currentAccount.getUltimateTTTList());
        }
        operate(ultTTTBoardManager);
    }

    private void operate(UltTTTBoardManager ultTTTBoardManager) {
        if (ultTTTBoardManager.buttonPressed.equals("Reset")) {
            initialize();
            enableAll();
            return;
        } else if (ultTTTBoardManager.buttonPressed.equals("Undo")) {
            if (ultTTTBoardManager.resetCells.equals("All"))
                initialize();
            else {
                ImageButtons[Integer.parseInt(ultTTTBoardManager.resetCells)].
                        setBackgroundResource(R.drawable.ult_clearimage);
            }
            if (!ultTTTBoardManager.resetBlockColor.equals("None")) {
                if (Integer.parseInt(ultTTTBoardManager.resetBlockColor) != Integer.MAX_VALUE)
                    tables[Integer.parseInt(ultTTTBoardManager.resetBlockColor)].
                            setBackgroundColor(Color.BLACK);
            }
        }
        if (ultTTTBoardManager.buttonPressed.equals("GameButton")) {
            if (ultTTTBoardManager.isP1Turn.equals("Player 1")) {
                ImageButtons[ultTTTBoardManager.currentCell].setBackgroundResource(R.drawable.ult_cross);
            } else {
                ImageButtons[ultTTTBoardManager.currentCell].setBackgroundResource(R.drawable.ult_o);
            }
        }
        changeTableColor(ultTTTBoardManager.nextActiveBlock, ultTTTBoardManager.currentActiveBlock,
                ultTTTBoardManager.isP1Turn, ultTTTBoardManager.buttonPressed);
        disableAll();
        enableBlock(ultTTTBoardManager.nextActiveBlock);
        disableWinnerBlocks(ultTTTBoardManager.disableBlock);
        disableUsedCells(ultTTTBoardManager.usedCells);
        setText(scoreP1, Integer.toString(ultTTTBoardManager.scoreP1));
        setText(scoreP2, Integer.toString(ultTTTBoardManager.scoreP2));

        if (ultTTTBoardManager.winner.equals("Player 1"))
            showToast(P1Name + " won this round");
        else if (ultTTTBoardManager.winner.equals("Player 2"))
            showToast(P2Name + " won this round");

        if (!ultTTTBoardManager.globalWinner.equals("None"))
            gameOver(ultTTTBoardManager.globalWinner);
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

    private void setText(TextView tv, String s) {
        tv.setText(s);
    }

    private void disableUsedCells(String[] used_cells) {
        for (String used_cell : used_cells) {
            try {
                disable(Integer.parseInt(used_cell));
            } catch (NumberFormatException e) {

            }
        }
    }

    private void disableWinnerBlocks(String disableBlock) {
        for (int i = 0; i < disableBlock.length(); i++)
            disableBlock(disableBlock.charAt(i) - 48);
    }

    private void enableBlock(int nextActiveBlock) {
        if (nextActiveBlock != Integer.MAX_VALUE)
            for (int i = nextActiveBlock * 9; i < (nextActiveBlock + 1) * 9; i++)
                enable(i);
        else {
            enableAll();
        }
    }

    private void disableBlock(int id) {
        for (int i = id * 9; i < (id + 1) * 9; i++)
            disable(i);
    }

    private void gameOver(String global_winner) {
        disableAll();
        AlertDialog ad = new AlertDialog.Builder(UltimateTTTGameActivity.this)
                .setTitle("WINNER!!!")
                .setMessage(getGlobalWinnerName(global_winner))
                .setPositiveButton("Restart Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Restart Game
                        initialize();
                        enableAll();
                        backend.execute(100);
                    }
                })
                .setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(UltimateTTTGameActivity.this, ScoreBoard.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                })
                //.setIcon(R.drawable.trophy)
                .show();
        ad.setCanceledOnTouchOutside(false);
    }

    private String getGlobalWinnerName(String global_winner) {
        if (global_winner.equals("Player 1"))
            return P1Name + " wins";
        else if (global_winner.equals("Player 2"))
            return P2Name + " wins";
        else if (global_winner.equals("Drawn"))
            return "Match Drawn";
        return "None";
    }

    private void showToast(String winner) {
        Toast.makeText(UltimateTTTGameActivity.this, winner, Toast.LENGTH_SHORT).show();
    }

    private int getIndex(ImageButton b) {
        for (int i = 0; i < ImageButtons.length; i++) {
            if (ImageButtons[i].equals(b)) {
                return i;
            }
        }
        if (b.equals(breset))
            return 100;
        if (b.equals(bundo))
            return 200;
        return -1;
    }
}
