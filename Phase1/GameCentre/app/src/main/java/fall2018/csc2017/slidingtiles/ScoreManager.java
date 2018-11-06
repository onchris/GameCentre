package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class to manage the lists of scores for display, orders user scores and game-wide
 * scores and prep lists of scores for display
 */
abstract class ScoreManager {
    protected Account currentAccount;
    protected List<Account> accountsList;
    protected List<Integer> userScores = new ArrayList<>();
    protected List<String> displayUserScoresList = new ArrayList<>();
    protected List<Pair<Integer, String>> gameScores = new ArrayList<>();
    protected List<String> displayGameScoresList = new ArrayList<>();
    protected boolean IS_GUEST;
    protected Integer score;

    public List<String> getDisplayUserScoresList() {
        return displayUserScoresList;
    }

    public List<String> getDisplayGameScoresList() {
        return displayGameScoresList;
    }

    public ScoreManager(String username, Context ctx, Integer score){
        this.score = score;
        accountsList = UtilityManager.loadAccountList(ctx);

        if(!username.equals("-1")) {
            IS_GUEST = false;
            for(Account account: accountsList) {
                if(account.getUsername().equals(username)) {
                    currentAccount = account;
                    userScores = currentAccount.getSlidingGameScores();
                    break;
                }
            }
            buildDisplayUserScoresList();
        } else {
            IS_GUEST = true;
        }
        buildGameScoresList();
        buildDisplayGameScoresList();
        buildDisplayUserScoresList();
    }

    abstract void buildGameScoresList();
    abstract void buildDisplayGameScoresList();
    abstract void buildDisplayUserScoresList();
}
