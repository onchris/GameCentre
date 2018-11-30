package fall2018.csc2017.slidingtiles.ObstacleDodger;
/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.content.Context;
import android.content.res.Resources;

/**
 * Manage obstacle utility, including get the height and width of screen
 */
public class ObUtilityManager {
    /**
     * The current context
     */
    public static Context CURRENT_CONTEXT;

    /**
     * Gets the screen width
     *
     * @return the screen width
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * Gets the screen height
     *
     * @return the screen height
     */
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
