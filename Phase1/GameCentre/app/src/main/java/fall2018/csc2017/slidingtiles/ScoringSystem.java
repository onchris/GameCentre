package fall2018.csc2017.slidingtiles;

/**
 * The scoring system of the game.
 */
public class ScoringSystem {

    /**
     * The highest possible score that users can get.
     */
    private int highestAchievable = 1000;

    /**
     * The lowest possible score that users can get.
     */
    private int lowestAchievable = 100;

    /**
     * Return the final score of the game.
     * @return the final score of the game
     */
    public int calculateScore(int movesTaken, int timeTaken) {
        int raw = highestAchievable - 2 * movesTaken - timeTaken;
        if (raw <= 100) {
            return lowestAchievable;
        } else {
            return raw;
        }
    }
}