package fall2018.csc2017.slidingtiles.ObstacleDodger;

/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import fall2018.csc2017.slidingtiles.Account;
import fall2018.csc2017.slidingtiles.R;
import fall2018.csc2017.slidingtiles.ScoreBoard;

import static android.os.Process.getThreadPriority;
import static android.os.Process.killProcess;
import static android.os.Process.myPid;

public class ObGameActivity extends AppCompatActivity {

    private GamePanel gamePanel;

    /**
     * Current user's account
     */
    private Account currentAccount;

    /**
     * Current user's account
     */
    //private Account currentAccount;

    private ObDodgerObserver observer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        gamePanel = new GamePanel(this);
        setContentView(gamePanel);

        if (getIntent().getSerializableExtra("account") != null) {
            currentAccount = (Account) getIntent().getSerializableExtra("account");
        }
        observer = new ObDodgerObserver(gamePanel, currentAccount, this);

        try {
            gamePanel.getManager().getGamePlayScene().addObserver(observer);
        } catch (NullPointerException e) {
            System.out.println("Exception occurred");
        }
    }

    public void createScoreBoard(int score, Bundle info){
        Intent scoreboard = new Intent(this, ScoreBoard.class);
        scoreboard.putExtra("currentUsername", info.getString("currentUsername"));
        scoreboard.putExtra("currentGame", info.getString("currentGame"));
        scoreboard.putExtra("currentScore", score);
        scoreboard.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(scoreboard);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        try {
            gamePanel.getThread().join();
            gamePanel.getThread().setRunning(false);
            super.onBackPressed();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }
}
