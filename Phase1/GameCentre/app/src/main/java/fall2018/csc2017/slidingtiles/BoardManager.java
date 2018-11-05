package fall2018.csc2017.slidingtiles;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import java.io.NotActiveException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
class BoardManager implements Serializable, Undoable {

    /**
     * The board being managed.
     */
    private Board board;

    /**
     * The number of steps that users can undo.
     */
    private int numCanUndo = 3;

    /**
     * The steps for users to undo.
     */
    private Stack<Integer> availableUndoSteps = new Stack<>();
    private transient ArrayList<Bitmap> customImageSet;
    private transient boolean useImage;

    public boolean isUseImage() {
        return useImage;
    }

    public void setUseImage(boolean useImage) {
        this.useImage = useImage;
    }

    public ArrayList<Bitmap> getCustomImageSet() {
        return customImageSet;
    }

    public void setCustomImageSet(ArrayList<Bitmap> customImageSet) {
        this.customImageSet = customImageSet;
    }

    /**
     * Gets the available number of steps that users can undo.
     * @return the available number of steps that users can undo
     */
    public int getNumCanUndo() {
        return numCanUndo;
    }

    /**
     * Manage a board that has been pre-populated.
     * @param board the board
     */
    public BoardManager(Board board) {
        this.board = board;
    }

    /**
     * Return the current board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Manage a new shuffled board.
     */
    public BoardManager() {
        List<Tile> tiles = new ArrayList<>();
        int numTiles = 4 * 4;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum));
        }

        Collections.shuffle(tiles);
        this.board = new Board(tiles);
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    boolean puzzleSolved() {
        boolean solved = true;
        int index = 1;
        Iterator<Tile> tileIterator = board.iterator();
        while(index != board.numTiles()) {
            if(index != tileIterator.next().getId())
            {
                solved = false;
            }
            index++;
        }
        return solved;
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    boolean isValidTap(int position) {

        int row = position / board.numRows;
        int col = position % board.numColumns;
        int blankId = board.numTiles();
        // Are any of the 4 the blank tile?
        Tile above = row == 0 ? null : board.getTile(row - 1, col);
        Tile below = row == board.numRows - 1 ? null : board.getTile(row + 1, col);
        Tile left = col == 0 ? null : board.getTile(row, col - 1);
        Tile right = col == board.numColumns - 1 ? null : board.getTile(row, col + 1);
        return (below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId);
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    void touchMove(int position) {
        int row = position / board.numRows;
        int col = position % board.numColumns;
        int blankId = board.numTiles();
        if(isValidTap(position))
        {
            availableUndoSteps.push(row);
            availableUndoSteps.push(col);
            Tile above = row == 0 ? null : board.getTile(row - 1, col);
            Tile below = row == board.numRows - 1 ? null : board.getTile(row + 1, col);
            Tile left = col == 0 ? null : board.getTile(row, col - 1);
            Tile right = col == board.numColumns - 1 ? null : board.getTile(row, col + 1);
            if(above != null && above.getId() == blankId)
            {
                board.swapTiles(row, col, row-1, col);
                availableUndoSteps.push(row - 1 );
                availableUndoSteps.push(col);
            }
            else if(below != null && below.getId() == blankId)
            {
                board.swapTiles(row, col, row+1, col);
                availableUndoSteps.push(row + 1);
                availableUndoSteps.push(col);
            }
            else if(left != null && left.getId() == blankId)
            {
                board.swapTiles(row, col, row, col-1);
                availableUndoSteps.push(row);
                availableUndoSteps.push(col - 1);
            }
            else if(right != null && right.getId() == blankId)
            {
                board.swapTiles(row, col, row, col+1);
                availableUndoSteps.push(row);
                availableUndoSteps.push(col + 1);
            }
        }
    }

    @Override
    public boolean canUndo() {
        return (!availableUndoSteps.isEmpty() && numCanUndo != 0);
    }

    @Override
    public void undo() {
        if (canUndo()){
            int col1 = availableUndoSteps.pop();
            int row1 = availableUndoSteps.pop();
            int col2 = availableUndoSteps.pop();
            int row2 = availableUndoSteps.pop();
            board.swapTiles(row1, col1, row2, col2);
            numCanUndo --;
        }
    }

}
