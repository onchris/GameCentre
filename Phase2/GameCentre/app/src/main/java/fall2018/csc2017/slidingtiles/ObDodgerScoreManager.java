package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.util.Pair;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Object to manage the scores of the accounts for display
 */

class ObDodgerScoreManager extends ScoreManager {

    /**
     * Constructor for a new obstacle dodger score manager
     * @param username the current account's username
     * @param ctx the context
     * @param score the score
     */
    ObDodgerScoreManager(String username, Context ctx, Integer score) {
        super(username, ctx, score);
        if (!username.equals("-1") && currentAccount != null) {
            userScores = currentAccount.getObstacleDodgerScores();
            buildDisplayUserScoresList();
        }
    }

    @Override
    void buildGameScoresList() {
        Pair<Integer, String> p;
        if (gameScores.size() == 0) {
            for (Account account : accountsList) {
                List<Integer> accountScores = account.getObstacleDodgerScores();
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

    @Override
    void buildDisplayGameScoresList() {
        if (displayGameScoresList.size() == 0) {
            String sep = ":      ";
            for (int i = 0; i <= gameScores.size() - 1; i++) {
                Pair<Integer, String> score = gameScores.get(i);
                String displayScore = score.second + sep + score.first.toString();
                displayGameScoresList.add(displayScore);
            }
        }
    }

    @Override
    void buildDisplayUserScoresList() {
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
