package fall2018.csc2017.slidingtiles;

/**
 * Implementing this interface allows a step to be undone.
 */
public interface Undoable {

    /**
     * Return whether this step can be undone.
     * @return whether this step can be undone
     */
    boolean canUndo();

    /**
     * Undo the step. Throws CannotUndoException if this step can not be undone.
     */
    void undo();

}
