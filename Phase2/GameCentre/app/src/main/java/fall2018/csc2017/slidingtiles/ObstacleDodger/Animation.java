package fall2018.csc2017.slidingtiles.ObstacleDodger;

/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Animation {

    /**
     * The bitmap array for the animation frames.
     */
    private Bitmap[] frames;

    /**
     * The index for the animation frame.
     */
    private int frameIndex;

    /**
     * The boolean indicating if the animation is playing.
     */
    private boolean isPlaying = false;

    /**
     * Return whether the animation is playing.
     *
     * @return boolean indicating whether animation is playing
     */
    boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Plays the animation.
     */
    void play() {
        isPlaying = true;
        frameIndex = 0;
        lastFrame = System.currentTimeMillis();
    }

    /**
     * Stops the animation.
     */
    void stop() {
        isPlaying = false;
    }

    /**
     * The long representing the frame time.
     */
    private float frameTime;

    /**
     * The long representing the last frame.
     */
    private long lastFrame;

    /**
     * Creates a new animation.
     *
     * @param frames   the frames of the animation.
     * @param animTime the animation time.
     */
    Animation(Bitmap[] frames, float animTime) {
        this.frames = frames;
        frameIndex = 0;

        frameTime = animTime / frames.length;

        lastFrame = System.currentTimeMillis();
    }

    /**
     * Draws the animation.
     *
     * @param canvas      the canvas to be drawn.
     * @param destination the destination of the rect
     */
    public void draw(Canvas canvas, Rect destination) {
        if (!isPlaying) {
            return;
        }

        scaleRect(destination);

        canvas.drawBitmap(frames[frameIndex], null, destination, new Paint());
    }

    /**
     * Scales a rectangle.
     *
     * @param rect a rect.
     */
    private void scaleRect(Rect rect) {
        float whRatio = (float) (frames[frameIndex].getWidth()) / frames[frameIndex].getHeight();
        if (rect.width() > rect.height()) {
            rect.left = rect.right - (int) (rect.height() * whRatio);
        } else {
            rect.top = rect.bottom - (int) (rect.width() * (1 / whRatio));
        }
    }

    /**
     * Updates the animation.
     */
    public void update() {
        if (!isPlaying) {
            return;
        }

        if (System.currentTimeMillis() - lastFrame > frameTime * 1000) {
            frameIndex++;
            frameIndex = frameIndex >= frames.length ? 0 : frameIndex;
            lastFrame = System.currentTimeMillis();
        }
    }
}

