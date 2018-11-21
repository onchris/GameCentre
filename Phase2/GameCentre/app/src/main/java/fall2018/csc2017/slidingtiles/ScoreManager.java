package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.util.Pair;
import java.util.ArrayList;
import java.util.List;

/**
 * A manager for the lists of scores to use in an adapter, orders user scores and game-wide
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

    /**
     * Gets the current account of current user
     */
    public Account getCurrentAccount() {
        return currentAccount;
    }

    /**
     * Gets the User's score list for display
     * @return List<String> of the user's scores
     */
    public List<String> getDisplayUserScoresList() {
        return displayUserScoresList;
    }

    /**
     * Gets the scores of everyone in the game
     * @return List<String> of the user's scores for everyone in the game
     */
    public List<String> getDisplayGameScoresList() {
        return displayGameScoresList;
    }

    /**
     * Sorts the lists of scores.
     * @param username
     * @param ctx
     * @param score
     */
    public ScoreManager(String username, Context ctx, Integer score){
        this.score = score;
        accountsList = UtilityManager.loadAccountList(ctx);

        if(!username.equals("-1")) {
            IS_GUEST = false;
            for(Account account: accountsList) {
                if(account.getUsername().equals(username)) {
                    currentAccount = account;
                    break;
                }
            }
        } else {
            IS_GUEST = true;
        }
        buildGameScoresList();
        buildDisplayGameScoresList();
    }

    /**
     * A method to populate and sort the GameScoresList with scores of all users playing the game.
     */
    abstract void buildGameScoresList();

    /**
     * A method to prepare the GameScoresList into a list of String.
     */
    abstract void buildDisplayGameScoresList();

    /**
     * A method to perpare the UserScoresList int a list of String.
     */
    abstract void buildDisplayUserScoresList();
}
