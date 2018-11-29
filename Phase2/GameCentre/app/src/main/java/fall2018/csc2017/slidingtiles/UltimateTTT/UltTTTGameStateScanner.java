package fall2018.csc2017.slidingtiles.UltimateTTT;

import java.util.ArrayList;
import java.util.Iterator;

class UltTTTGameStateScanner {

    /**
     * The initializer for ultimate tic tac toe game
     */
    private UltTTTBackendInit initializer;
    /**
     * The activity for ultimate tic tac toe game
     */
    private UltimateTTTGameActivity activity;

    /**
     * The game state scanner for ultimate tic tac toe game
     *
     * @param initializer the initializer for ultimate tic tac toe game
     * @param activity    the activity for ultimate tic tac toe game
     */
    UltTTTGameStateScanner(UltTTTBackendInit initializer, UltimateTTTGameActivity activity) {
        this.initializer = initializer;
        this.activity = activity;
    }

    /**
     * Gets the global winner
     *
     * @return the global winner
     */
    String findGlobalWinner() {
        int no_drawn = findOccurrences("Drawn", initializer.result);
        int no_win_p1 = findOccurrences("Player 1", initializer.result);
        int no_win_p2 = findOccurrences("Player 2", initializer.result);
        int deciding_factor = (9 - no_drawn) / 2;

        if (no_win_p1 > deciding_factor)
            return "Player 1";
        else if (no_win_p2 > deciding_factor)
            return "Player 2";
        else if (initializer.result.size() == 9 && no_win_p1 == no_win_p2)
            return "Drawn";
        else
            return "None";
    }

    /**
     * Gets the global winner's name
     *
     * @return the global winner's name
     */
    String getGlobalWinnerName(String global_winner) {
        if (global_winner.equals("Player 1"))
            return activity.P1Name + " wins";
        else if (global_winner.equals("Player 2"))
            return activity.P2Name + " wins";
        else if (global_winner.equals("Drawn"))
            return "Match Drawn";
        return "None";
    }

    /**
     * Gets the number of occurrences of certain string
     *
     * @param str  the string to be find
     * @param list the list contains the string str
     * @return the number of occurrences of string str in list
     */
    private int findOccurrences(String str, ArrayList list) {
        String curr_item;
        int num_occurrences;
        Iterator itr;
        num_occurrences = 0;
        itr = list.iterator();
        while (itr.hasNext()) {
            curr_item = (String) itr.next();
            if (curr_item.equals(str))
                num_occurrences++;
        }
        return num_occurrences;
    }
}
