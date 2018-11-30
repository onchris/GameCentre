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

import fall2018.csc2017.slidingtiles.Account;
import fall2018.csc2017.slidingtiles.ScoreBoard;

/**
 * Class for Obstacle Dodger game activity.
 */
public class ObGameActivity extends AppCompatActivity {

    /**
     * The game panel for the game activity.
     */
    private GamePanel gamePanel;

    /**
     * Current user's account
     */
    private Account currentAccount;


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
        ObDodgerObserver observer = new ObDodgerObserver(gamePanel, currentAccount, this);

        try {
            gamePanel.getManager().getGamePlayScene().addObserver(observer);
        } catch (NullPointerException e) {
            System.out.println("Exception occurred");
        }
    }

    /**
     * Creates a scoreboard for the current user.
     *
     * @param score the score of the user.
     * @param info  the account and game information.
     */
    public void createScoreBoard(int score, Bundle info) {
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
        gamePanel.getThread().interrupt();
        gamePanel.getThread().setRunning(false);
        super.onBackPressed();
    }
}
