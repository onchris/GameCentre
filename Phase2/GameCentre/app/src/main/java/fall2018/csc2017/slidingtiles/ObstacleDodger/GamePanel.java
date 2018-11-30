package fall2018.csc2017.slidingtiles.ObstacleDodger;

/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import fall2018.csc2017.slidingtiles.R;

/**
 * Class for Obstacle Dodger game panel.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * The thread for the game panel.
     */
    private MainThread thread;

    /**
     * The SceneManager for the game panel.
     */
    private SceneManager manager;

    /**
     * Indicates whether a game panel is created
     */
    Boolean receive;

    /**
     * A bitmap image.
     */
    Bitmap bgr;

    /**
     * A default bitmap overlay.
     */
    Bitmap overlayDefault;

    /**
     * A bitmap overlay.
     */
    Bitmap overlay;

    /**
     * A paint object.
     */
    Paint pTouch;

    /**
     * The bitmaps X coordinate.
     */
    int X = -100;

    /**
     * The bitmaps Y Coordinate.
     */
    int Y = -100;

    /**
     * A canvas for the logo.
     */
    Canvas c2;

    /**
     * Creates a new game panel.
     *
     * @param context the context for the game panel
     */
    public GamePanel(Context context) {
        super(context);
        receive = true;
        bgr = BitmapFactory.decodeResource(getResources(), R.drawable.aliengreen);
        overlayDefault = BitmapFactory.decodeResource(getResources(), R.drawable.tile_16).copy(Bitmap.Config.ARGB_8888, true);
        overlay = BitmapFactory.decodeResource(getResources(), R.drawable.aliengreen).copy(Bitmap.Config.ARGB_8888, true);
        c2 = new Canvas(overlay);
        pTouch = new Paint(Paint.ANTI_ALIAS_FLAG);
        pTouch.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        pTouch.setColor(Color.TRANSPARENT);
        pTouch.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));

        getHolder().addCallback(this);

        ObUtilityManager.CURRENT_CONTEXT = context;

        thread = new MainThread(getHolder(), this);

        manager = new SceneManager();

        setFocusable(true);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (true) {
            try {
                receive = false;
                thread.interrupt();
                thread.setRunning(false);
                retry = false;
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                X = (int) event.getX();
                Y = (int) event.getY();
                invalidate();
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                X = (int) event.getX();
                Y = (int) event.getY();
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:
                break;
        }
        if (!receive)
            return false;
        manager.receiveTouch(event);
        return true;
    }

    /**
     * Updates the Scene Manager.
     */
    public void update() {
        manager.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        manager.draw(canvas);

        canvas.drawBitmap(bgr, getWidth() - bgr.getWidth(), 0, null);
        c2.drawBitmap(overlayDefault, getWidth() - bgr.getWidth(), 0, null);
        c2.drawCircle(X, Y, 80, pTouch);
        canvas.drawBitmap(overlay, getWidth() - bgr.getWidth(), 0, null);
    }

    /**
     * Returns the Scene Manager.
     *
     * @return returns the Scene Manager
     */
    public SceneManager getManager() {
        return manager;
    }

    /**
     * Returns the main thread.
     *
     * @return returns the thread.
     */
    public MainThread getThread() {
        return thread;
    }
}
