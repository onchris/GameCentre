package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AccountTest {
    private Account initialAccount = new Account("123", "dummy_password");
    private List<Integer> emptyScore = new ArrayList<>();
    @Test
    public void testGetAllInstanceVariables() {
        assertNotNull(initialAccount.getUsername());
        assertEquals("123", initialAccount.getUsername());
        assertNotEquals("1234", initialAccount.getUsername());
        assertEquals(new Integer(0), initialAccount.getUltimateTTTScores());
        assertEquals(emptyScore, initialAccount.getObstacleDodgerScores());
        assertEquals(emptyScore, initialAccount.getSlidingGameScores());
        assertEquals(new ArrayList<BoardManager>(), initialAccount.getBoardList());
        assertEquals("dummy_password", initialAccount.getPassword());
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
    }

    @Test
    public void setUltimateTTTSave() {
    }

    @Test
    public void equals() {
    }

    @Test
    public void addToSlidingGameScores() {
    }

    @Test
    public void sortGameScores() {
    }

    @Test
    public void addToObDodgeGameScores() {
    }

    @Test
    public void getUltimateTTTSave() {
    }
}