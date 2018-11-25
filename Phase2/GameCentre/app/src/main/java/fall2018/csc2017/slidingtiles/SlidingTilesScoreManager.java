package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.util.Pair;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A score manager for the sliding tiles game, populates and sorts the lists of scores.
 */
public class SlidingTilesScoreManager extends ScoreManager {

    /**
     * Sort the scores in a sliding tiles game
     * @param username username of the player
     * @param ctx context of the activity
     * @param score score of the last game
     */
    SlidingTilesScoreManager(String username, Context ctx, Integer score) {
        super(username, ctx, score);
        if (!username.equals("-1")) {
            userScores = currentAccount.getSlidingGameScores();
            buildDisplayUserScoresList();
        }
    }

    /**
     * Storing the game-wide scores in a List of Pair of user's score and username
     */
    @Override
    protected void buildGameScoresList() {
        Pair<Integer, String> p;
        if (gameScores.size() == 0) {
            for (Account account : accountsList) {
                List<Integer> accountScores = account.getSlidingGameScores();
                for (int i = 0; i <= accountScores.size() - 1; i++) {
                    p = new Pair<>(accountScores.get(i), account.getUsername());
                    gameScores.add(p);
                }
            }
        }
        if (IS_GUEST) {
            try {
                p = new Pair<>(score, "Guest");
                gameScores.add(p);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(gameScores, new Comparator<Pair<Integer, String>>() {
            @Override
            public int compare(Pair<Integer, String> p1, Pair<Integer, String> p2) {
                return p2.first.compareTo(p1.first);
            }
        });
    }

    /**
     * A method to convert from a list of pairs to a list of strings for display
     */
    @Override
    protected void buildDisplayGameScoresList() {
        if (displayGameScoresList.size() == 0) {
            String sep = ":      ";
            for (int i = 0; i <= gameScores.size() - 1; i++) {
                Pair<Integer, String> score = gameScores.get(i);
                String displayScore = score.second + sep + score.first.toString();
                displayGameScoresList.add(displayScore);
            }
        }
    }

    /**
     * A method to convert the list of user's game scores into a list of strings for display
     */
    @Override
    protected void buildDisplayUserScoresList() {
        currentAccount.sortGameScores();
        if (displayUserScoresList.size() == 0) {
            String sep = ":      ";
            for (int i = 0; i <= userScores.size() - 1; i++) {
                Integer score = userScores.get(i);
                String displayScore = currentAccount.getUsername() + sep + score.toString();
                displayUserScoresList.add(displayScore);
            }
        }
    }
}
