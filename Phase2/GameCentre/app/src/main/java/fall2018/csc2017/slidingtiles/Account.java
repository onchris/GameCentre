package fall2018.csc2017.slidingtiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The account of the user.
 */
public class Account implements Serializable {
    /**
     * Account's username
     */
    private String username;
    /**
     * Account's password
     */
    private String password;
    /**
     * Account's saved sliding tile games
     */
    private List<BoardManager> boardList = new ArrayList<>();

    /**
     * Account's saved ultimate tic tac toe games
     */
    private List<Integer> ultimateTTTSave = new ArrayList<>();

    /**
     * Account's sliding tile games' scores as a list
     */
    private List<Integer> slidingGameScores = new ArrayList<>();

    /**
     * Account's ultimate tic tac toe games' total wins
     */
    private Integer ultimateTTTScores;

    /**
     * Account's obstacle dodger games' scores as a list
     */
    private List<Integer> obstacleDodgerScores = new ArrayList<>();

    /**
     * Account constructor
     *
     * @param username user's name
     * @param password user's password
     */
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        ultimateTTTScores = 0;
    }

    /**
     * Gets this account's username
     *
     * @return the username as String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets this account's list of scores for ultimate tic tac toe games
     *
     * @return this account's list of scores for ultimate tic tac toe games
     */
    Integer getUltimateTTTScores() {
        return ultimateTTTScores;
    }

    /**
     * Updates the score for the current account.
     *
     * @param won checks if the player won the UltTTT game.
     */
    void ultimateTTTWinUpdate(Boolean won) {
        if (won) ultimateTTTScores++;
    }

    /**
     * Gets this account's list of scores for obstacle dodger games
     *
     * @return this account's list of scores for obstacle dodger games
     */
    List<Integer> getObstacleDodgerScores() {
        return obstacleDodgerScores;
    }

    /**
     * Gets this account's password
     *
     * @return the password as String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets this account's list of scores
     *
     * @return this account's list of scores
     */
    List<Integer> getSlidingGameScores() {
        return this.slidingGameScores;
    }

    /**
     * Gets this account's list of sliding game's boards
     *
     * @return the List of Boards
     */
    public List<BoardManager> getBoardList() {
        return boardList;
    }

    /**
     * Sets this account's list of sliding game's boards
     *
     * @param boardList the board to be replaced with
     */
    public void setBoardList(List<BoardManager> boardList) {
        this.boardList = boardList;
    }

    /**
     * Sets this account's list of ultimate tic tac toe game's boards
     *
     * @param ultTTTUserResponses the board to be replaced with
     */
    void setUltimateTTTSave(int ultTTTUserResponses) {
        if (ultTTTUserResponses < 0)
            this.ultimateTTTSave = new ArrayList<>();
        else {
            this.ultimateTTTSave.add(ultTTTUserResponses);
        }

    }

    /**
     * Assert with this account's username is equal to the account to be compared with
     *
     * @param account the account to be compared with
     * @return The equality test with this account's username and param account's username
     */
    public boolean equals(Account account) {
        return account != null && account.getUsername().equals(this.getUsername());
    }

    /**
     * Add a score from a game of sliding tiles to the list of the user's scores
     *
     * @param score the score to add to sliding games
     */
    void addToSlidingGameScores(int score) {
        this.slidingGameScores.add(score);
        sortGameScores();
    }

    /**
     * Sort the user's score lists in reverse order from highest to lowest
     */
    void sortGameScores() {
        Collections.sort(slidingGameScores, Collections.<Integer>reverseOrder());
        Collections.sort(obstacleDodgerScores, Collections.<Integer>reverseOrder());
    }

    /**
     * Add a score from a game of obstacle dodger to the list of user's scores
     *
     * @param score the score to add to sliding games
     */
    public void addToObDodgeGameScores(int score) {
        obstacleDodgerScores.add(score);
        sortGameScores();
    }

    /**
     * Returns a list of UltimateTTT saved moves
     *
     * @return returns the UltimateTTT save.
     */
    public List getUltimateTTTSave() {
        return ultimateTTTSave;
    }

}
