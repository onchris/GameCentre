package fall2018.csc2017.slidingtiles.ObstacleDodger;

/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.graphics.Canvas;

/**
 * Interface for Obstacle Dodger game objects.
 */
public interface GameObject {

    /**
     * Draws a game object.
     *
     * @param canvas the canvas to be drawn.
     */
    void draw(Canvas canvas);

    /**
     * Updates a game object.
     */
    void update();
}
