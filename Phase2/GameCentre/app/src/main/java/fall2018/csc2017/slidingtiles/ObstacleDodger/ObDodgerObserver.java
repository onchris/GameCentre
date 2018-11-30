package fall2018.csc2017.slidingtiles.ObstacleDodger;

import android.os.Bundle;

import java.util.Observable;
import java.util.Observer;

import fall2018.csc2017.slidingtiles.Account;

import static fall2018.csc2017.slidingtiles.UtilityManager.saveObDodgerScoresToAccounts;

/*
Class to observe whether the obstacle dodger game is over.
 */
public class ObDodgerObserver implements Observer {

    /*
    The game activity of the observer.
     */
    private ObGameActivity activity;

    /*
    The game panel of the observer.
     */
    private GamePanel gamePanel;

    /*
    The current account of the observer.
     */
    private Account currentAccount;

    /*
    Creates a new observer.
     */
    ObDodgerObserver(GamePanel gamePanel, Account account, ObGameActivity activity) {
        this.gamePanel = gamePanel;
        this.currentAccount = account;
        this.activity = activity;
    }

    @Override
    public void update(Observable o, Object arg) {
        Integer score = (Integer) arg;
        Bundle infoBundle = new Bundle();

        if(currentAccount != null) {
            infoBundle.putString("currentUsername", currentAccount.getUsername());
            currentAccount.addToObDodgeGameScores(score);
            saveObDodgerScoresToAccounts(activity, currentAccount, score);
        }
        else {
            infoBundle.putString("currentUsername", "-1");
        }
        infoBundle.putString("currentGame", "obDodger");
        o.deleteObservers();
        gamePanel.surfaceDestroyed(gamePanel.getHolder());
        activity.createScoreBoard(score, infoBundle);
    }
}
