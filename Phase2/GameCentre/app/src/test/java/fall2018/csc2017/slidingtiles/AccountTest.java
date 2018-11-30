package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AccountTest {
    private Account initialAccount = new Account("123", "dummy_password");
    private List<Integer> emptyScore = new ArrayList<>();

    /**
     * General test for account creation that variables are not null.
     * Extensive testing for individual components are tested below this.
     */
    @Test
    public void testGetAllInitialVariables() {
        assertNotNull(initialAccount.getUsername());
        assertEquals("123", initialAccount.getUsername());
        assertNotEquals("1234", initialAccount.getUsername());
        assertEquals(new Integer(0), initialAccount.getUltimateTTTScores());
        assertEquals(emptyScore, initialAccount.getObstacleDodgerScores());
        assertEquals(emptyScore, initialAccount.getSlidingGameScores());
        assertEquals(new ArrayList<BoardManager>(), initialAccount.getBoardList());
        assertEquals("dummy_password", initialAccount.getPassword());
        assertEquals(new ArrayList(), initialAccount.getUltimateTTTSave());
    }

    @Test
    public void ultimateTTTWinUpdate() {
        assertEquals(new Integer(0), initialAccount.getUltimateTTTScores());
        initialAccount.ultimateTTTWinUpdate(true);
        assertEquals(new Integer(1), initialAccount.getUltimateTTTScores());
        initialAccount.ultimateTTTWinUpdate(false);
        assertEquals(new Integer(1), initialAccount.getUltimateTTTScores());
    }

    @Test
    public void setBoardList() {
        List<BoardManager> boardList = new ArrayList<>();
        boardList.add(BoardSetup.setUp4x4Solved());
        assertEquals(new ArrayList<BoardManager>(), initialAccount.getBoardList());
        initialAccount.setBoardList(boardList);
        assertNotEquals(new ArrayList<BoardManager>(), initialAccount.getBoardList());
        assertEquals(boardList, initialAccount.getBoardList());
    }

    @Test
    public void setUltimateTTTSave() {
        assertEquals(new ArrayList(), initialAccount.getUltimateTTTSave());
        initialAccount.setUltimateTTTSave(-1);
        assertEquals(new ArrayList(), initialAccount.getUltimateTTTSave());
        ArrayList arrayList = new ArrayList();
        arrayList.add(1);
        initialAccount.setUltimateTTTSave(1);
        assertEquals(arrayList, initialAccount.getUltimateTTTSave());
    }

    @Test
    public void equals() {
        Account secondAccount = new Account("Bogus", "123");
        assertFalse(initialAccount.equals(secondAccount));
        secondAccount = new Account("123", "123");
        assertTrue(initialAccount.equals(secondAccount));
        assertFalse(initialAccount.equals(null));
    }

    @Test
    public void addToSlidingGameScores() {
        ArrayList<Integer> scoreList = new ArrayList<>();
        assertEquals(scoreList, initialAccount.getSlidingGameScores());
        initialAccount.addToSlidingGameScores(0);
        scoreList.add(0);
        assertEquals(scoreList, initialAccount.getSlidingGameScores());
        initialAccount.addToSlidingGameScores(10);
        assertEquals(2, initialAccount.getSlidingGameScores().size());
        assertEquals(10, (int) initialAccount.getSlidingGameScores().get(0));
        initialAccount.addToSlidingGameScores(100);
        assertEquals(100, (int) initialAccount.getSlidingGameScores().get(0));
        assertEquals(0, (int) initialAccount.getSlidingGameScores().get(2));
    }

    @Test
    public void addToObDodgeGameScores() {
        assertEquals(emptyScore, initialAccount.getObstacleDodgerScores());
        emptyScore.add(100);
        initialAccount.addToObDodgeGameScores(100);
        assertEquals(emptyScore, initialAccount.getObstacleDodgerScores());
        initialAccount.addToObDodgeGameScores(1000);
        assertEquals(1000, (int) initialAccount.getObstacleDodgerScores().get(0));
    }
}