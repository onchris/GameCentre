package fall2018.csc2017.slidingtiles;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests for BoardManager
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
        board4x4.undo();
        assertEquals(0 ,board4x4.getMoves());
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

    /**
     * Tests undo logic.
     */
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

    /**
     * Tests whether or not Board is using image as tiles.
     */
    @Test
    public void isUsingImage(){
        assertFalse(board4x4.isUseImage());
        assertFalse(board30x30.isUseImage());
        board4x4.setUseImage(true);
        assertTrue(board4x4.isUseImage());
    }

    /**
     * Tests getter for image set where should be null
     */
    @Test
    public void testNullImageSet(){
        assertNull(board4x4.getCustomImageSet());
        assertNull(board30x30.getCustomImageSet());
        board4x4.setCustomImageSet(new ArrayList<Bitmap>());
        assertNotNull(board4x4.getCustomImageSet());
    }

    @Test
    public void getSetTimeSpent(){
        assertEquals(0, board4x4.getTimeSpent());
        board4x4.setTimeSpent(123);
        assertNotNull(board4x4.getTimeSpent());
    }


    /**
     * Test custom rule creation of board
     */
    @Test
    public void customBoardCreation() {
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = 6 * 7;
        for (int tileNum = 0; tileNum != numTiles-1; tileNum++) {
            tiles.add(new Tile(tileNum));
        }
        tiles.add(new Tile(6*7, null));
        Board customBoard = new Board(tiles, 6, 7);
        customBoard.getTile(5,5).setBackground(null);
        assertTrue(customBoard.getTile(5,5).hasBackground());
        BoardManager customBoardManager = new BoardManager(customBoard);
        assertEquals("[1, 2, 3, 4, 5, 6, " +
                "7, 8, 9, 10, 11, 12, " +
                "13, 14, 15, 16, 17, 18, " +
                "19, 20, 21, 22, 23, 24, " +
                "25, 26, 27, 28, 29, 30, " +
                "31, 32, 33, 34, 35, 36, " +
                "37, 38, 39, 40, 41, 42]", tiles.toString());

        assertEquals("Board{tiles=1, 2, 3, 4, 5, 6," +
                " 7, 8, 9, 10, 11, 12," +
                " 13, 14, 15, 16, 17, 18," +
                " 19, 20, 21, 22, 23, 24," +
                " 25, 26, 27, 28, 29, 30," +
                " 31, 32, 33, 34, 35, 36," +
                " 37, 38, 39, 40, 41, 42}", customBoardManager.getBoard().toString());
        assertEquals("6x7", customBoard.getTilesDimension());
        assertNull(customBoard.getTile(5,5).getBackground());
        assertNull(customBoard.getTile(5,6).getBackground());
        assertEquals(1, customBoard.getTile(5,5).compareTo(customBoard.getTile(5,6)));
    }

    /**
     * Test for touch move
     */
    @Test
    public void testTouchMove(){
        board4x4.touchMove(14);
        board4x4.touchMove(10);
        board4x4.touchMove(11);
        board4x4.touchMove(15);
    }
}

