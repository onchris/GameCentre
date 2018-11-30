package fall2018.csc2017.slidingtiles.ObstacleDodger;

/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Class for Obstacle Dodger Animation Manager.
 */
public class AnimationManager {

    /**
     * An array containing animations.
     */
    private Animation[] animations;

    /**
     * The index of an animation.
     */
    private int animationIndex = 0;

    /**
     * Constructs a new animation manager.
     *
     * @param animations the animations of the manager.
     */
    AnimationManager(Animation[] animations) {
        this.animations = animations;
    }

    /**
     * Plays the animation that is in the input index.
     *
     * @param index the index to be played.
     */
    void playAnim(int index) {
        for (int i = 0; i < animations.length; i++) {
            if (i == index) {
                if (!animations[index].isPlaying()) {
                    animations[i].play();
                }
            } else {
                animations[i].stop();
            }
        }
        animationIndex = index;
    }

    /**
     * Draws the animation at the animation index.
     *
     * @param canvas the canvas to be drawn.
     * @param rect   the rect to be drawn.
     */
    public void draw(Canvas canvas, Rect rect) {
        if (animations[animationIndex].isPlaying()) {
            animations[animationIndex].draw(canvas, rect);
        }
    }

    /**
     * Updates the animation at the animation index.
     */
    public void update() {
        if (animations[animationIndex].isPlaying()) {
            animations[animationIndex].update();
        }
    }

}
