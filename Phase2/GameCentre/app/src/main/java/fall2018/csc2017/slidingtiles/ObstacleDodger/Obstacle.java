package fall2018.csc2017.slidingtiles.ObstacleDodger;
/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Class for obstacles.
 */
public class Obstacle implements GameObject {
    /**
     * The rectangle that is one part of obstacle
     */
    private Rect rectangle;
    /**
     * The rectangle that is the other part of obstacle
     */
    private Rect rectangle2;
    /**
     * The color of obstacle
     */
    private int color;

    /**
     * Gets a rectangle
     *
     * @return the rectangle
     */
    Rect getRectangle() {
        return rectangle;
    }

    /**
     * Increases obstacle's size by y
     *
     * @param y the amount to be added to obstacle
     */
    void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;
        rectangle2.top += y;
        rectangle2.bottom += y;
    }

    /**
     * The obstacle
     *
     * @param rectHeight the height of obstacle
     * @param color      the color of obstacle
     * @param startX     the start point x
     * @param startY     the start point y
     * @param playerGap  the gap for player to pass through
     */
    public Obstacle(int rectHeight, int color, int startX, int startY, int playerGap) {
        this.color = color;
        rectangle = new Rect(0, startY, startX, startY + rectHeight);
        rectangle2 = new Rect(startX + playerGap, startY, ObUtilityManager.getScreenWidth(), startY + rectHeight);
    }

    /**
     * Check if the player collides with obstacle
     *
     * @param player the player
     * @return if the player collides with obstacle
     */
    boolean playerCollide(RectPlayer player) {
        return Rect.intersects(rectangle, player.getRectangle()) || Rect.intersects(rectangle2, player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
        canvas.drawRect(rectangle2, paint);
    }

    @Override
    public void update() {
    }
}
