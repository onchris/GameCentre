package fall2018.csc2017.slidingtiles.ObstacleDodger;

/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.Observable;

public class GameplayScene extends Observable implements Scene {

    /*
    The rectangle that contains the player animation.
     */
    private Rect r = new Rect();

    /*
    The rect player.
     */
    private RectPlayer player;

    /*
    The point where the rectangle spawns.
     */
    private Point playerPoint;

    /*
    The obstacle manager for the current game.
     */
    private ObstacleManager obstacleManager;

    /*
    Boolean that represents whether the player is moving.
     */
    private boolean movingPlayer = false;

    /*
    Boolean that represents whether game is over.
     */
    private boolean gameOver = false;

    /*
    The long representing the time after game finished.
     */
    private long gameOverTime;

    /*
    Creates a new gameplay scene.
     */
    GameplayScene() {
        player = new RectPlayer(new Rect(100, 100, 200, 200), Color.rgb(255, 0, 0));
        playerPoint = new Point(ObUtilityManager.getScreenWidth() / 2, 3 * ObUtilityManager.getScreenHeight() / 4);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);
    }

    /*
    Resets the gameplay scene, placing the player back at the start position and create new obstacles.
     */
    private void reset() {
        playerPoint = new Point(ObUtilityManager.getScreenWidth() / 2, 3 * ObUtilityManager.getScreenHeight() / 4);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);
        movingPlayer = false;
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
        deleteObservers();
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        if(!gameOver)
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!gameOver && player.getRectangle().contains((int) event.getX(), (int) event.getY()))
                        movingPlayer = true;
                    if (gameOver && System.currentTimeMillis() - gameOverTime >= 2000) {
                        reset();
                        gameOver = true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!gameOver && movingPlayer)
                        playerPoint.set((int) event.getX(), (int) event.getY());
                    break;
                case MotionEvent.ACTION_UP:
                    movingPlayer = false;
                    break;
            }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        player.draw(canvas);
        obstacleManager.draw(canvas);

        if (gameOver) {
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.MAGENTA);
            String text = "Game Over";
            drawCenterText(canvas, paint, text);
        }

    }

    @Override
    public void update() {
        if (!gameOver) {
            player.update(playerPoint);
            obstacleManager.update();
            if (obstacleManager.playerCollide(player)) {
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
                setChanged();
                notifyObservers(obstacleManager.getScore());
            }
        }
    }

    /*
    Draws a text in the center of the screen.
     */
    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

}
