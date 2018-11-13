package fall2018.csc2017.slidingtiles;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
     * The number of moves that users have taken.
     */
    private int moves = 0;

    private long timeSpent = 0;

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

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public void setCustomImageSet(ArrayList<Bitmap> customImageSet) {
        this.customImageSet = customImageSet;
    }

    /**
     * Gets the total number of moves that users have taken.
     * @return the total number of moves that users have taken
     */
    public int getMoves() {
        return moves;
    }

    /**
     * Gets the available number of steps that users can undo.
     * @return the available number of steps that users can undo
     */
    public int getNumCanUndo() {
        return numCanUndo;
    }

    /**
     * sets the available number of steps that users can undo.
     */
    public void setNumCanUndo(int numCanUndo){
        this.numCanUndo = numCanUndo;
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
    private boolean puzzleSolvable(){
        int columns = board.getNumColumns();
        int rows = board.getNumRows();
        int inversion = 0;
        ArrayList<Integer> currentColumn = new ArrayList<>();
        for(Tile t : this.board){
            if(currentColumn.size()-1 < columns)
                currentColumn.add(t.getId());

        }
        return true;
    }
    private int getRowInversions(ArrayList<Integer> intList){
        int count = 0;
        Set<Integer> checkedDigits = new HashSet<>();
        int returnInt = 0;
        while(count < intList.size()){
            int current_digit = intList.get(count);
            if(current_digit == 1)
                checkedDigits.add(current_digit);
            else{

            }
            count++;
        }
        return 1;
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

        int row = position / board.getNumColumns();
        int col = position - row* board.getNumColumns();
        int blankId = board.numTiles();
        // Are any of the 4 the blank tile?
        Tile above = row == 0 ? null : board.getTile(row - 1, col);
        Tile below = row == board.getNumRows() - 1 ? null : board.getTile(row + 1, col);
        Tile left = col == 0 ? null : board.getTile(row, col - 1);
        Tile right = col == board.getNumColumns() - 1 ? null : board.getTile(row, col + 1);
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
        int row = position / board.getNumColumns();
        int col = position - row* board.getNumColumns();
        int blankId = board.numTiles();
        if(isValidTap(position))
        {
            availableUndoSteps.push(row);
            availableUndoSteps.push(col);
            Tile above = row == 0 ? null : board.getTile(row - 1, col);
            Tile below = row == board.getNumRows() - 1 ? null : board.getTile(row + 1, col);
            Tile left = col == 0 ? null : board.getTile(row, col - 1);
            Tile right = col == board.getNumColumns() - 1 ? null : board.getTile(row, col + 1);
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
            moves ++;
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
            moves ++;
        }
    }

}
