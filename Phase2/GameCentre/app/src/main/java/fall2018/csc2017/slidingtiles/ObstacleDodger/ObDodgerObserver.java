package fall2018.csc2017.slidingtiles.ObstacleDodger;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.List;
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
