package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.widget.Toast;


/**
 * Movement controller to process taps
 */
public class MovementController {

    /**
     * the board manager this movement controller controlling
     */
    private BoardManager boardManager = null;

    /**
     * constructor for a new movement controller
     */
    public MovementController() {
    }

    /**
     * connect the board manager for this movement controller
     * @param boardManager the board manager to be controlled
     */
    public void setBoardManager(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    /**
     * A checker for if the movement is valid
     * @param context the context of the game
     * @param position position of the tap
     * @param display the display
     */
    public void processTapMovement(Context context, int position, boolean display) {
        if (boardManager.isValidTap(position)) {
            boardManager.touchMove(position);

            if (boardManager.puzzleSolved()) {
                Toast.makeText(context, context.getString(R.string.mc_win), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, context.getString(R.string.mc_invalid_tap), Toast.LENGTH_SHORT).show();
        }
    }
}
