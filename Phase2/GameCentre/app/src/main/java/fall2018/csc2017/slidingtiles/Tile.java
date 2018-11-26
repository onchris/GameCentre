package fall2018.csc2017.slidingtiles;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * A Tile in a sliding tiles puzzle.
 */
public class Tile implements Comparable<Tile>, Serializable {

    /**
     * The background id to find the tile image.
     */
    private transient Drawable background;

    /**
     * The unique id.
     */
    private int id;

    /**
     * A check if the tile has a background
     */
    private transient boolean hasBackground;

    /**
     * A Tile with id and background. The background may not have a corresponding image.
     *
     * @param id         the id
     * @param background the background
     */
    public Tile(int id, Drawable background) {
        this.id = id;
        this.background = background;
        hasBackground = true;
    }

    /**
     * A tile with a background id; look up and set the id.
     *
     * @param backgroundId
     */
    public Tile(int backgroundId) {
        id = backgroundId + 1;
    }

    /**
     * Return the background id.
     *
     * @return the background id
     */
    public Drawable getBackground() {
        return background;
    }

    /**
     * Set the background of the tile.
     */
    public void setBackground(Drawable bg) {
        this.background = bg;
        this.hasBackground = true;
    }

    /**
     * Return the tile id.
     *
     * @return the tile id
     */
    public int getId() {
        return id;
    }

    /**
     * Return if the tile has a background.
     *
     * @return true if the tile has a background
     */
    public boolean hasBackground() {
        return hasBackground;
    }

    @Override
    public int compareTo(@NonNull Tile o) {
        return o.id - this.id;
    }

    @Override
    public String toString(){
        return "" + getId();
    }
}
