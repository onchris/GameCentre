package fall2018.csc2017.slidingtiles.ObstacleDodger;

/*
Adapted from:
https://www.youtube.com/watch?v=OojQitoAEXs - Retro Chicken Android Studio 2D Game Series
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class ObstacleManager {
    //higher index = lower on screen = higher y value
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;

    private long startTime;
    private long initTime;

    private int score = 0;
    public static ArrayList<Integer> allScores = new ArrayList<>();

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color) {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();

        populateObstacles();
    }

    public boolean playerCollide(RectPlayer player) {
        for (Obstacle ob : obstacles) {
            if (ob.playerCollide(player))
                //allScores.add(score);
                return true;
        }
        return false;
    }

    private void populateObstacles() {
        int currY = -5 * ObUtilityManager.getScreenHeight() / 4;
        while (currY < 0) {
            int xStart = (int) (Math.random() * (ObUtilityManager.getScreenWidth() - playerGap));
            obstacles.add(new Obstacle(obstacleHeight, color, xStart, currY, playerGap));
            currY += obstacleHeight + obstacleGap;
        }
    }

    public void update() {
        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float) (Math.sqrt(1 + (startTime - initTime) / 2000.0)) * ObUtilityManager.getScreenHeight() / (10000.0f);
        for (Obstacle ob : obstacles) {
            ob.incrementY(speed * elapsedTime);
        }
        if (obstacles.get(obstacles.size() - 1).getRectangle().top >= ObUtilityManager.getScreenHeight()) {
            int xStart = (int) (Math.random() * (ObUtilityManager.getScreenWidth() - playerGap));
            obstacles.add(0, new Obstacle(obstacleHeight, color, xStart, obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap, playerGap));
            obstacles.remove(obstacles.size() - 1);
            score++;
        }
    }

    public void draw(Canvas canvas) {
        for (Obstacle ob : obstacles)
            ob.draw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.MAGENTA);
        canvas.drawText("" + score, 50, 50 + paint.descent() - paint.ascent(), paint);
    }

    public int getScore() {
        return score;
    }
}
