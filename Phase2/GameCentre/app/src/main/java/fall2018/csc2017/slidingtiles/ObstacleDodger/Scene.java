package fall2018.csc2017.slidingtiles.ObstacleDodger;

/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Interface for game scenes.
 */
public interface Scene {

    /**
     * Updates the game scene.
     */
    void update();

    /**
     * Draws the game scene
     *
     * @param canvas The canvas that is drawn.
     */
    void draw(Canvas canvas);

    /**
     * The action that is made to the scene.
     *
     * @param event The action made.
     */
    void receiveTouch(MotionEvent event);
}
