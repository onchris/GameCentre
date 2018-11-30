package fall2018.csc2017.slidingtiles;

/*
Adapted from:
https://github.com/DaveNOTDavid/sample-puzzle/blob/master/app/src/main/java/com/davenotdavid/samplepuzzle/GestureDetectGridView.java

This extension of GridView contains built in logic for handling swipes between buttons
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Gridview with logic for handling swipes between buttons
 */
public class GestureDetectGridView extends GridView {
    /**
     * The minimum distance for swiping
     */
    public static final int SWIPE_MIN_DISTANCE = 100;
    /**
     * The maximum off path
     */
    public static final int SWIPE_MAX_OFF_PATH = 100;
    /**
     * The swipe threshold velocity
     */
    public static final int SWIPE_THRESHOLD_VELOCITY = 100;
    /**
     * The Gesture detector
     */
    private GestureDetector gDetector;
    /**
     * movement controller for this grid view
     */
    private MovementController mController;
    /**
     * if the movement fling is confirmed
     */
    private boolean mFlingConfirmed = false;
    /**
     * movement touch x and y coordinates
     */
    private float mTouchX, mTouchY;
    /**
     * The board manager
     */
    private BoardManager boardManager;

    /**
     * The grid view for gesture detector
     *
     * @param context the context
     */
    public GestureDetectGridView(Context context) {
        super(context);
        init(context);
    }

    /**
     * The grid view for gesture detector
     *
     * @param context the context
     * @param attrs   the attribute set
     */
    public GestureDetectGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * The grid view for gesture detector
     *
     * @param context      the context
     * @param attrs        the attribute set
     * @param defStyleAttr the default style attribute
     */
    public GestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) // API 21
    public GestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr,
                                 int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * Initialize movement controller and gesture detector
     *
     * @param context the context
     */
    private void init(final Context context) {
        mController = new MovementController();
        gDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                int position = GestureDetectGridView.this.pointToPosition
                        (Math.round(event.getX()), Math.round(event.getY()));

                mController.processTapMovement(context, position, true);
                return true;
            }

            @Override
            public boolean onDown(MotionEvent event) {
                return true;
            }

        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        gDetector.onTouchEvent(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mFlingConfirmed = false;
        } else if (action == MotionEvent.ACTION_DOWN) {
            mTouchX = ev.getX();
            mTouchY = ev.getY();
        } else {

            if (mFlingConfirmed) {
                return true;
            }

            float dX = (Math.abs(ev.getX() - mTouchX));
            float dY = (Math.abs(ev.getY() - mTouchY));
            if ((dX > SWIPE_MIN_DISTANCE) || (dY > SWIPE_MIN_DISTANCE)) {
                mFlingConfirmed = true;
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return gDetector.onTouchEvent(ev);
    }

    public void setBoardManager(BoardManager boardManager) {
        this.boardManager = boardManager;
        mController.setBoardManager(boardManager);
    }
}
