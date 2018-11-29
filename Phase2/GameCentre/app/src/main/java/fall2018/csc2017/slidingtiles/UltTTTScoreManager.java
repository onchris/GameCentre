package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UltTTTScoreManager extends ScoreManager {

    UltTTTScoreManager(String username, Context ctx, Integer score){
        super(username, ctx, score);
        if (!username.equals("-1") && currentAccount != null) {
            userScores.add(currentAccount.getUltimateTTTScores());
            buildDisplayUserScoresList();
        }
    }

    @Override
    void buildGameScoresList() {
        Pair<Integer, String> p;
        if (gameScores.size() == 0) {
            for (Account account : accountsList) {
                p = new Pair<>(account.getUltimateTTTScores(), account.getUsername());
                gameScores.add(p);
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
        displayUserScoresList.add(currentAccount.getUltimateTTTScores().toString());

    }
}
