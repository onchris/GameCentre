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
     * Account's statistics, number of puzzles solved and scores achieved in sliding tiles
     */
    public int numSolved, highscore;
    /**
     * Account's username
     */
    private String username;
    /**
     * Account's password
     */
    private String password;
    /**
     * Account's saved games
     */
    private ArrayList<BoardManager> boardList = new ArrayList<>();
    /**
     * Account's scores as a list
     */
    private List<Integer> slidingGameScores = new ArrayList<>();

    /**
     * Account constructor
     *
     * @param username user's name
     * @param password user's password
     */
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        numSolved = 0;
        highscore = 0;
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
    public List<Integer> getSlidingGameScores() {
        return this.slidingGameScores;
    }

    /**
     * Gets this account's list of sliding game's boards
     *
     * @return the ArrayList of Boards
     */
    public ArrayList<BoardManager> getBoardList() {
        return boardList;
    }

    /**
     * Sets this account's list of sliding game's boards
     *
     * @param boardList the board to be replaced with
     */
    public void setBoardList(ArrayList<BoardManager> boardList) {
        this.boardList = boardList;
    }

    /**
     * Add to this account's list of sliding game's boards
     *
     * @param board the board to be added to this account's list of boards
     */
    public void addToBoardList(BoardManager board) {
        if (!boardList.contains(board))
            boardList.add(board);
    }

    /**
     * Assert with this account's username is equal to the account to be compared with
     *
     * @param account the account to be compared with
     * @return The equality test with this account's username and param account's username
     */
    public boolean equals(Account account) {
        return account.getUsername().equals(this.getUsername());
    }
    /**
     * Add a score from a game of sliding tiles to the list of the user's scores
     *
     * @param score
     */
    public void addToSlidingGameScores(int score) {
        this.slidingGameScores.add(score);
        Collections.sort(this.slidingGameScores, Collections.<Integer>reverseOrder());
    }

    /**
     * Sort the user's SlidingTiles in reverse order from highest to lowest
     */
    public void sortSlidingGameScores() {
        Collections.sort(this.slidingGameScores, Collections.<Integer>reverseOrder());
    }
}
