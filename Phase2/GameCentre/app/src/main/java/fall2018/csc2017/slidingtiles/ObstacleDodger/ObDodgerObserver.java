package fall2018.csc2017.slidingtiles.ObstacleDodger;

import android.content.Intent;

import java.util.Observable;
import java.util.Observer;

import fall2018.csc2017.slidingtiles.Account;
import fall2018.csc2017.slidingtiles.ScoreBoard;

import static fall2018.csc2017.slidingtiles.UtilityManager.saveObDodgerScoresToAccounts;

public class ObDodgerObserver implements Observer {

    ObGameActivity activity;
    GamePanel gamePanel;
    Account currentAccount;

    ObDodgerObserver(GamePanel gamePanel, Account account, ObGameActivity activity) {
        this.gamePanel = gamePanel;
        this.currentAccount = account;
        this.activity = activity;
    }


    @Override
    /**
     * On notified GameplayScene has changed, save score to account and change to scoreboard
     */
    public void update(Observable o, Object arg) {
        Integer score = (Integer) arg;
        Intent tmp = new Intent(activity, ScoreBoard.class);
        if (currentAccount!=null) {
            currentAccount.addToObDodgeGameScores(score);
            saveObDodgerScoresToAccounts(activity, currentAccount, score);
            tmp.putExtra("currentUsername", currentAccount.getUsername());
        } else {
            tmp.putExtra("currentUsername", "-1");
        }
        tmp.putExtra("currentGame", "obDodger");
        tmp.putExtra("currentScore", score.toString());
        activity.startActivity(tmp);
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }
}
