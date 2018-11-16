package fall2018.csc2017.slidingtiles;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BoardAndTileTest {

    /** The board manager for testing. */
    private BoardManager board4x4, shuffledBoard4x4, board30x30, shuffledBoard30x30;

    /**
     * Make a set of tiles that are in order and out of order.
     */
    @Before
    public void setUpCorrect(){
        board4x4 = BoardSetup.setUp4x4Solved();
        shuffledBoard4x4 = BoardSetup.setUp4x4Unsolved();
        board30x30 = BoardSetup.setUp30x30Solved();
        shuffledBoard30x30 = BoardSetup.setUp30x30Unsolved();
    }

    /**
     * Shuffle first two tiles.
     * @param b the board to be tested
     */
    public void swapFirstTwo(Board b){
        assertEquals(1, b.getTile(0,0).getId());
        assertEquals(2, b.getTile(0, 1).getId());
        b.swapTiles(0,0,0,1);
        assertEquals(2, b.getTile(0,0).getId());
        assertEquals(1, b.getTile(0, 1).getId());
    }

    /**
     * Swaps the last two tiles in the specific board.
     * @param b the board to be tested
     */
    public void swapLastTwo(Board b){
        assertEquals(b.getNumColumns()*b.getNumRows()-1,
                b.getTile(b.getNumRows()-1, b.getNumColumns()-2).getId());
        assertEquals(b.getNumColumns()*b.getNumRows(),
                b.getTile(b.getNumRows()-1, b.getNumColumns()-1).getId());
        b.swapTiles(b.getNumRows()-1, b.getNumColumns()-1, b.getNumRows()-1, b.getNumColumns()-2);
        assertEquals(b.getNumColumns()*b.getNumRows(),
                b.getTile(b.getNumRows()-1, b.getNumColumns()-2).getId());
        assertEquals(b.getNumColumns()*b.getNumRows()-1,
                b.getTile(b.getNumRows()-1, b.getNumColumns()-1).getId());
    }

    /**
     * Test whether swapping two tiles makes a solved board unsolved for board size of 30
     */
    @Test
    public void test4x4IsSolved() {
        assertTrue(board4x4.puzzleSolved());
        swapFirstTwo(board4x4.getBoard());
        assertFalse(board4x4.puzzleSolved());
        assertFalse(shuffledBoard4x4.puzzleSolved());
    }

    /**
     * Test whether swapping two tiles makes a solved board unsolved for board size of 30
     */
    @Test
    public void test30x30IsSolved() {
        assertTrue(board30x30.puzzleSolved());
        swapFirstTwo(board30x30.getBoard());
        assertFalse(board30x30.puzzleSolved());
        assertFalse(shuffledBoard30x30.puzzleSolved());
    }


    /**
     * Test whether swapping the first two tiles works.
     */
    @Test
    public void swapFirstTwo() {
        swapFirstTwo(board4x4.getBoard());
        swapFirstTwo(board30x30.getBoard());
    }

    /**
     * Test whether swapping the last two tiles works.
     */
    @Test
    public void swapLastTwo(){
        swapLastTwo(board4x4.getBoard());
        swapLastTwo(board30x30.getBoard());
    }

    /**
     * Test whether isValidTap works.
     */
    @Test
    public void testIsValidTap() {
        assertTrue(board4x4.isValidTap(11));
        assertTrue(board4x4.isValidTap(14));
        assertFalse(board4x4.isValidTap(10));
    }
    @Test
    public void testUndo(){
        assertNotEquals(0, board4x4.getNumCanUndo());
        board4x4.setNumCanUndo(1);
        assertTrue(board4x4.isValidTap(14));
        assertFalse(board4x4.canUndo());
        board4x4.touchMove(14);
        assertFalse(board4x4.isValidTap(14));
        assertTrue(board4x4.canUndo());
        swapFirstTwo(board4x4.getBoard());
        board4x4.undo();
        assertFalse(board4x4.canUndo());
    }
}

