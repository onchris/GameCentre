package fall2018.csc2017.slidingtiles.ObstacleDodger;

/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.graphics.Canvas;

public interface GameObject {

    /*
    Draws a game object.
     */
    void draw(Canvas canvas);

    /*
    Updates a game object.
     */
    void update();
}
