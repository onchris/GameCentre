package fall2018.csc2017.slidingtiles.ObstacleDodger;

/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

import fall2018.csc2017.slidingtiles.R;

/**
 * Class for a rect player.
 */
public class RectPlayer implements GameObject {

    /**
     * The player rectangle.
     */
    private Rect rectangle;

    /**
     * The player color.
     */
    private int color;

    /**
     * The animation manager for player rectangle.
     */
    private AnimationManager animManager;

    /**
     * Returns the rectangle.
     *
     * @return Rectangle is returned.
     */
    Rect getRectangle() {
        return rectangle;
    }

    /**
     * Create a new rect player.
     *
     * @param rectangle The rectangle of the player.
     * @param color     The color of the player.
     */
    RectPlayer(Rect rectangle, int color) {
        this.rectangle = rectangle;
        this.color = color;

        Bitmap idleImg = BitmapFactory.decodeResource(ObUtilityManager.CURRENT_CONTEXT.getResources(), R.drawable.aliengreen);
        Bitmap walk1 = BitmapFactory.decodeResource(ObUtilityManager.CURRENT_CONTEXT.getResources(), R.drawable.aliengreen_walk1);
        Bitmap walk2 = BitmapFactory.decodeResource(ObUtilityManager.CURRENT_CONTEXT.getResources(), R.drawable.aliengreen_walk2);

        Animation idle = new Animation(new Bitmap[]{idleImg}, 2);
        Animation walkRight = new Animation(new Bitmap[]{walk1, walk2}, 0.5f);

        Matrix m = new Matrix();
        m.preScale(-1, 1);
        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);
        walk2 = Bitmap.createBitmap(walk2, 0, 0, walk1.getWidth(), walk2.getHeight(), m, false);

        Animation walkLeft = new Animation(new Bitmap[]{walk1, walk2}, 0.5f);

        animManager = new AnimationManager(new Animation[]{idle, walkRight, walkLeft});
    }

    @Override
    public void draw(Canvas canvas) {
        animManager.draw(canvas, rectangle);
    }

    @Override
    public void update() {
        animManager.update();
    }

    /**
     * Update the point of the rectangle.
     *
     * @param point a coordinate point.
     */
    public void update(Point point) {
        float oldLeft = rectangle.left;

        rectangle.set(point.x - rectangle.width() / 2, point.y - rectangle.height() / 2, point.x + rectangle.width() / 2, point.y + rectangle.height() / 2);

        int state = 0;
        if (rectangle.left - oldLeft > 5) {
            state = 1;
        } else if (rectangle.left - oldLeft < -5) {
            state = 2;
        }

        animManager.playAnim(state);
        animManager.update();
    }
}

