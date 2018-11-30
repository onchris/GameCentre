package fall2018.csc2017.slidingtiles.ObstacleDodger;

/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Interface for Obstacle Dodger game play scene
 */
public interface Scene {
    /**
     * Updates a scene
     */
    public void update();

    /**
     * Draws a game scene.
     *
     * @param canvas the canvas to be drawn.
     */
    public void draw(Canvas canvas);

    /**
     * To terminate a scene
     */
    public void terminate();

    /**
     * Receive the touch from player
     *
     * @param event the motion taken by player
     */
    public void receiveTouch(MotionEvent event);
}
