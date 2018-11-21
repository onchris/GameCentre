package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.util.Pair;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ObDodgerScoreManager extends ScoreManager {

    /**
     * Sort the scores in an obstacle dodger game
     * @param username
     * @param ctx
     * @param score
     */
    public ObDodgerScoreManager(String username, Context ctx, Integer score) {
        super(username, ctx, score);
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
                p = new Pair<>(-1, "Guest");
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
        if (displayGameScoresList.size() == 0) {
            String sep = ":      ";
            for (int i = 0; i <= userScores.size() - 1; i++) {
                Integer score = userScores.get(i);
                String displayScore = currentAccount.getUsername() + sep + score.toString();
                displayUserScoresList.add(displayScore);
            }
        }
    }
}
