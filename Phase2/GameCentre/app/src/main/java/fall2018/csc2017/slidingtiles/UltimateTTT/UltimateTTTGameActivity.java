package fall2018.csc2017.slidingtiles.UltimateTTT;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONObject;

import fall2018.csc2017.slidingtiles.Account;
import fall2018.csc2017.slidingtiles.GameSelection;
import fall2018.csc2017.slidingtiles.R;

import fall2018.csc2017.slidingtiles.UtilityManager;

/**
 * Adapted from: https://github.com/Prakash2403/UltimateTicTacToe/blob/master/app/src/main/java/com/example/prakash/ultimatetictactoe/frontend/Fifth.java
 */

public class UltimateTTTGameActivity extends AppCompatActivity implements View.OnClickListener {
    private UltTTTBoardManager ultTTTBoardManager;
    private UltTTTConnector connector;
    private ImageButton[] ImageButtons;
    private TableLayout tables[];

    private Account currentAccount;
    private boolean IS_GUEST = false;

    String P1Name;
    String P2Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_uttt_main);

        connector = new UltTTTConnector(this);
        ImageButtons = connector.getImageButtons();
        tables = connector.getTables();
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
        index = getIndex(curr_button);
        runFrontEnd(index);
        ultTTTBoardManager.operate();
    }


    public void runFrontEnd(int index) {
        JSONObject response;
        response = connector.backend.executer.execute(index);
        ultTTTBoardManager = new UltTTTBoardManager(UltimateTTTInfoManager.parseJson(response), connector);
        if (!IS_GUEST) {
            UtilityManager.saveUltimateTTTUltTTTBoardManager(this, currentAccount, currentAccount.getUltimateTTTList());
        }
    }


    public void setText(TextView tv, String s) {
        tv.setText(s);
    }


    public String getGlobalWinnerName(String global_winner) {
        if (global_winner.equals("Player 1"))
            return P1Name + " wins";
        else if (global_winner.equals("Player 2"))
            return P2Name + " wins";
        else if (global_winner.equals("Drawn"))
            return "Match Drawn";
        return "None";
    }


    private int getIndex(ImageButton b) {
        for (int i = 0; i < ImageButtons.length; i++) {
            if (ImageButtons[i].equals(b)) {
                return i;
            }
        }
        if (b.equals(connector.breset))
            return 100;
        if (b.equals(connector.bundo))
            return 200;
        return -1;
    }
}
