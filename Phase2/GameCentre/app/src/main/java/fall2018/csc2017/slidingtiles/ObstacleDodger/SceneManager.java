package fall2018.csc2017.slidingtiles.ObstacleDodger;

/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Manage the scene, including receive touch movement from player, draw, update and get the scene.
 */
public class SceneManager {

    /**
     * List of scenes
     */
    private List<Scene> scenes = new ArrayList<>();

    /**
     * The active scene
     */
    private static int ACTIVE_SCENE;

    /**
     * Manager of the scene
     */
    SceneManager() {
        ACTIVE_SCENE = 0;
        scenes.add(new GameplayScene());
    }

    /**
     * Receive the touch from player
     *
     * @param event the motion taken by player
     */
    void receiveTouch(MotionEvent event) {
        scenes.get(ACTIVE_SCENE).receiveTouch(event);
    }

    /**
     * Updates the scene
     */
    public void update() {
        scenes.get(ACTIVE_SCENE).update();
    }

    /**
     * Draw the scene
     *
     * @param canvas the canvas to draw on
     */
    public void draw(Canvas canvas) {
        scenes.get(ACTIVE_SCENE).draw(canvas);
    }

    /**
     * Gets the game play scene
     *
     * @return the game play scene
     */
    GameplayScene getGamePlayScene() {
        if (scenes.get(ACTIVE_SCENE) instanceof GameplayScene)
            return (GameplayScene) scenes.get(ACTIVE_SCENE);
        return null;
    }
}
