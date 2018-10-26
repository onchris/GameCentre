package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

import java.util.List;

public class CustomScrollView extends ScrollView {
    public static final int SWIPE_MIN_DISTANCE = 100;
    private float mTouchX, mTouchY;
    private GestureDetector gDetector;
    private boolean mFlingConfirmed;
    private static int boxWidth = 100, boxHeight = 100;
    private List<Board> boardList;
    private Rect backgroundFill;
    private Paint backgroundColor = new Paint(Color.BLUE);

    public CustomScrollView(Context context){
        super(context);
        init(context);
    }
    public CustomScrollView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    public CustomScrollView(Context context, AttributeSet attrs, int defaultAtt){
        super(context, attrs, defaultAtt);
        init(context);
    }
    public CustomScrollView(Context context, AttributeSet attrs, int defaultAtt, int defStyleRes){
        super(context, attrs, defaultAtt, defStyleRes);
        init(context);
    }
    private void init(final Context context){
        gDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener());
        this.getLayoutParams().height = 100;


    }
    public void setLoadingList(List<Board> boardList){
        this.boardList = boardList;
    }
    private void createLoadButton(Context context){

    }
    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawRect(backgroundFill, backgroundColor);
    }
    @Override
    protected void onMeasure(int width, int height){
        setMeasuredDimension(width, height/2);
        boxHeight = height;
        boxWidth = width;
        backgroundFill = new Rect(0,0,boxWidth,boxHeight);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev){
        return gDetector.onTouchEvent(ev);
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
}
