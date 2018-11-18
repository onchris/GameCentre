package ObstacleDodger;

/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Scene {
    public void update();

    public void draw(Canvas canvas);

    public void terminate();

    public void receiveTouch(MotionEvent event);
}
