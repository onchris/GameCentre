package fall2018.csc2017.slidingtiles.ObstacleDodger;

/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Class for Obstacle Dodger main thread.
 */
public class MainThread extends Thread {

    /**
     * The Max fps for the thread.
     */
    private static final int MAX_FPS = 30;

    /**
     * The surface holder for the thread.
     */
    private final SurfaceHolder surfaceHolder;

    /**
     * The game panel for the thread.
     */
    private GamePanel gamePanel;

    /**
     * Boolean indicating whether the thread is running.
     */
    private boolean running;

    /**
     * The static variable for the game canvas.
     */
    public static Canvas canvas;

    /**
     * Sets the running boolean variable.
     *
     * @param running the variable indicating whether the thread is running.
     */
    void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Creates a main thread for the game.
     *
     * @param surfaceHolder the surface holder for the thread
     * @param gamePanel     the game panel for the thread
     */
    MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime = 0;
        long timeMillis = 1000 / MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000 / MAX_FPS;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;
            if (!running) {
                return;
            }
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        timeMillis = (System.nanoTime() - startTime) / 1000000;
        waitTime = targetTime - timeMillis;
        try {
            if (waitTime > 0)
                sleep(waitTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        totalTime += System.nanoTime() - startTime;
        frameCount++;
        if (frameCount == MAX_FPS) {
            double averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
            frameCount = 0;
            totalTime = 0;
            System.out.println(averageFPS);
        }

    }
}

