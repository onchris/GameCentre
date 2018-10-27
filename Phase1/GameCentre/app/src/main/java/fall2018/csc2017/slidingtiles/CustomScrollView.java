package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.widget.ListView;

import java.util.List;

public class CustomScrollView extends ListView {
    private final Drawable background =
            ResourcesCompat.getDrawable(getResources(), R.drawable.bg_simplebg, null);
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
        background.setAlpha(100);
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }
    @Override
    protected void onMeasure(int width, int height){
        setBackground(background);
        setMeasuredDimension(width, height);
    }
}
