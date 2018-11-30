package fall2018.csc2017.slidingtiles.UltimateTTT;

//Adapted from: https://github.com/Prakash2403/UltimateTicTacToe/blob/master/app/src/main/java/com/example/prakash/ultimatetictactoe/frontend/Fifth.java

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TableLayout;

import org.json.JSONObject;

import java.util.List;

import fall2018.csc2017.slidingtiles.Account;
import fall2018.csc2017.slidingtiles.R;

/**
 * Class for Ultimate Tic Tac Toe game activity.
 */
public class UltimateTTTGameActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * The board manager for ultimate tic tac toe game
     */
    private UltTTTBoardManager ultTTTBoardManager;

    /**
     * The connector for ultimate tic tac toe game
     */
    private UltTTTConnector connector;

    /**
     * The image buttons
     */
    private ImageButton[] ImageButtons;

    /**
     * The tables
     */
    private TableLayout tables[];

    /**
     * The current account
     */
    private Account currentAccount;
    /**
     * A check if the user is a guest
     */
    public boolean IS_GUEST = false;

    /**
     * The player1's name
     */
    String P1Name;

    /**
     * The player2's name
     */
    String P2Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_uttt_main);

        if (getIntent().hasExtra("account")) {
            IS_GUEST = false;
            currentAccount = (Account) getIntent().getSerializableExtra("account");
            P1Name = currentAccount.getUsername();
            P2Name = "Guest";
            if (currentAccount.getUltimateTTTSave().size() != 0) {
                List saveFile = currentAccount.getUltimateTTTSave();
                connector = new UltTTTConnector(this);
                ImageButtons = connector.getImageButtons();
                tables = connector.getTables();
                initialize();
                runFrontEnd(saveFile);
            } else {
                connector = new UltTTTConnector(this);
                ImageButtons = connector.getImageButtons();
                tables = connector.getTables();
                initialize();
            }
        } else {
            IS_GUEST = true;
            P1Name = "Guest1";
            P2Name = "Guest2";
            connector = new UltTTTConnector(this);
            ImageButtons = connector.getImageButtons();
            tables = connector.getTables();
            initialize();
        }
    }

    /**
     * Initialize connector with scores and button states
     */
    public void initialize() {
        connector.scoreP1.setText("0");
        connector.scoreP2.setText("0");
        for (ImageButton ImageButton : ImageButtons) {
            ImageButton.setOnClickListener(this);
            ImageButton.setBackgroundResource(R.drawable.ult_clearimage);
        }
        for (TableLayout tableLayout : tables)
            tableLayout.setBackgroundColor(Color.BLACK);
        connector.breset.setOnClickListener(this);
        connector.breset.setEnabled(true);
        connector.bundo.setOnClickListener(this);
        connector.bundo.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        int index;
        ImageButton curr_button;
        curr_button = findViewById(v.getId());
        index = connector.getIndex(curr_button);
        runFrontEnd(index);
        ultTTTBoardManager.operate();
    }


    /**
     * Responds to the button pressed
     *
     * @param index the index of button pressed
     */
    public void runFrontEnd(int index) {
        JSONObject response;
        response = connector.backend.executer.execute(index);
        ultTTTBoardManager = new UltTTTBoardManager(UltimateTTTInfoManager.parseJson(response), connector);
    }

    /**
     * Runs the saved moves to return to old game state.
     *
     * @param index the index of the move.
     */
    public void runFrontEnd(List<Integer> index) {
        JSONObject response;
        for (int i : index) {
            response = connector.backend.executer.execute(i);
            ultTTTBoardManager = new UltTTTBoardManager(UltimateTTTInfoManager.parseJson(response), connector);
            ultTTTBoardManager.operate();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * Returns the current account.
     *
     * @return returns the current account.
     */
    public Account getCurrentAccount() {
        return currentAccount;
    }
}
