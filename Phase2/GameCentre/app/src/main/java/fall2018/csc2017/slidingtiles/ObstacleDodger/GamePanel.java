package fall2018.csc2017.slidingtiles.ObstacleDodger;

/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import fall2018.csc2017.slidingtiles.Board;
import fall2018.csc2017.slidingtiles.R;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;

    private SceneManager manager;
    Boolean receive;

    Bitmap bgr;
    Bitmap overlayDefault;
    Bitmap overlay;
    Paint pTouch;
    int X = -100;
    int Y = -100;
    Canvas c2;

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!receive)
            return false;
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
        if(receive)
            manager.receiveTouch(event);
        return true;
    }

    public void update() {
        manager.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        manager.draw(canvas);

        //draw background
        canvas.drawBitmap(bgr, canvas.getWidth() - bgr.getWidth(), 0, null);
        //copy the default overlay into temporary overlay and punch a hole in it
        c2.drawBitmap(overlayDefault, canvas.getWidth() - bgr.getWidth(), 0, null); //exclude this line to show all as you draw
        c2.drawCircle(X, Y, 80, pTouch);
        //draw the overlay over the background
        canvas.drawBitmap(overlay, canvas.getWidth() - bgr.getWidth(), 0, null);
    }

    public SceneManager getManager() {
        return manager;
    }

    public MainThread getThread(){return thread;}
}
