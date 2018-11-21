package fall2018.csc2017.slidingtiles.ObstacleDodger;

/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Observable;
import java.util.Observer;

import fall2018.csc2017.slidingtiles.Account;
import fall2018.csc2017.slidingtiles.GameSelection;
import fall2018.csc2017.slidingtiles.ScoreBoard;

import static fall2018.csc2017.slidingtiles.UtilityManager.saveObDodgerScoresToAccounts;

public class TiltGameActivity extends AppCompatActivity implements Observer {

    private GamePanel gamePanel;

    /**
     * Current user's account
     */
    private Account currentAccount;

    /**
     * Current user's account
     */
    //private Account currentAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        gamePanel = new GamePanel(this);
        setContentView(gamePanel);

        try {
            gamePanel.getManager().getGamePlayScene().addObserver(this);
        } catch (NullPointerException e) {
            System.out.println("Exception occurred");
        }

        if (!GameSelection.IS_GUEST) {
            currentAccount = (Account) getIntent().getSerializableExtra("account");
        }

    }

    @Override
    public void onBackPressed() {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    /**
     * On notified GameplayScene has changed, save score to account and change to scoreboard
     */
    public void update(Observable o, Object arg) {
        Integer score = (Integer) arg;
        Intent tmp = new Intent(this, ScoreBoard.class);
        if (!GameSelection.IS_GUEST) {
            currentAccount.addToObDodgeGameScores(score);
            saveObDodgerScoresToAccounts(this, currentAccount, score);
            tmp.putExtra("currentUsername", currentAccount.getUsername());
        } else {
            tmp.putExtra("currentUsername", "-1");
        }
        tmp.putExtra("currentGame", "obDodger");
        tmp.putExtra("currentScore", score.toString());
        startActivity(tmp);

        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }
}
