package fall2018.csc2017.slidingtiles;

import org.json.JSONObject;
import org.junit.Test;

import java.util.Map;

import fall2018.csc2017.slidingtiles.UltimateTTT.UltTTTBackendInit;
import fall2018.csc2017.slidingtiles.UltimateTTT.UltTTTGameStateScanner;
import fall2018.csc2017.slidingtiles.UltimateTTT.UltTTTGameStates;
import fall2018.csc2017.slidingtiles.UltimateTTT.UltimateTTTBackend;
import fall2018.csc2017.slidingtiles.UltimateTTT.UltimateTTTGameActivity;
import fall2018.csc2017.slidingtiles.UltimateTTT.UltimateTTTInfoManager;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit testing for Ultimate Tic Tac Toe
 */
public class UltimateTTTTest {

    /**
     * Initialize a game activity.
     */
    UltimateTTTGameActivity activity = new UltimateTTTGameActivity();
    /**
     * Initialize an backend initializer.
     */
    UltTTTBackendInit init = new UltTTTBackendInit();

    /**
     * Initialize a gamestate scanner.
     */
    UltTTTGameStateScanner scanner = new UltTTTGameStateScanner(init, activity);

    /**
     * Initialize a gamestate.
     */
    UltTTTGameStates gamestate = new UltTTTGameStates(init, scanner);

    /**
     * Initialize a backend.
     */
    UltimateTTTBackend backend = new UltimateTTTBackend(activity);

    /**
     * Test that history stack adds objects update history is called.
     */
    @Test
    public void updateHistory() {
        JSONObject ob = new JSONObject();
        gamestate.updateHistory(ob);
        assertEquals(1, init.history.size());
    }

    /**
     * Tests that score changes for correct user.
     */
    @Test
    public void updateScore() {
        gamestate.updateScore(0);
        assertEquals(1, init.score[0]);
        assertEquals(0, init.score[1]);
    }

    /**
     * Tests that the board updates with correct value.
     */
    @Test
    public void updateBoard() {
        gamestate.updateBoard(2, 2, 2);
        assertEquals(0, init.boardStatus[2][2][2]);
        assertFalse(init.boardStatus[2][2][2] == 1);
    }

    /**
     * Tests that the game updates turn correctly.
     */
    @Test
    public void updateTurn() {
        gamestate.updateTurn();
        assertTrue(!init.isP1Turn);
    }

    /**
     * Tests that game returns winner.
     */
    @Test
    public void getWinner() {
        String winner = gamestate.getWinner(2);
        assertEquals("None", winner);
    }

    /**
     * Tests that the method parses a json object.
     */
    @Test
    public void parseJSON() {
        JSONObject ob = new JSONObject();
        Map parsed = UltimateTTTInfoManager.parseJson(ob);
        assertNull(parsed.get("Current Winner"));
        assertNull(parsed.get("DisableBlock"));
    }

    /**
     * Tests that the game resets.
     */
    @Test
    public void executeReset(){
        JSONObject ob = new JSONObject();
        backend.executer.initializer.history.push(ob);
        assertEquals(1, backend.executer.initializer.history.size());
        backend.executer.executeReset();
        assertEquals(0, backend.executer.initializer.history.size());
    }
}
