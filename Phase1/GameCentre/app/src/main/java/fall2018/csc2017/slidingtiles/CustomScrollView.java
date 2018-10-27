package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.List;

public class CustomScrollView extends ListView {
    public static final int SWIPE_MIN_DISTANCE = 100;
    private float mTouchX, mTouchY;
    private GestureDetector gDetector;
    private boolean mFlingConfirmed;
    private static int boxWidth = 100, boxHeight = 100;
    private List<Board> boardList;
    private final Drawable background = ResourcesCompat.getDrawable(getResources(), R.drawable.bg_simplebg, null);
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
        background.setAlpha(100);
    }
    public void setLoadingList(List<Board> boardList){
        this.boardList = boardList;
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }
    @Override
    protected void onMeasure(int width, int height){
        boxHeight = height;
        boxWidth = width;
        setBackground(background);
        setMeasuredDimension(width, height);
    }
}
