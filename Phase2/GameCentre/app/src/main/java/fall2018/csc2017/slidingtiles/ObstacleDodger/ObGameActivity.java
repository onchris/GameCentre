package fall2018.csc2017.slidingtiles.ObstacleDodger;

/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import fall2018.csc2017.slidingtiles.Account;

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

    @Override
    public void onBackPressed() {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }
}
