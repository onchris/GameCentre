package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.Nullable;
import android.widget.Button;

import java.util.ArrayList;

public interface Tileable {
    enum DigitEnum
    {
        DIGIT_ONES, DIGIT_TENS, DIGIT_THOU
    }
    public Drawable generateBackground(boolean isBlank, Tile tile);
    public Drawable generateImageBackground(boolean isBlank, Tile tile, @Nullable Bitmap imageBitmap);
    public LayerDrawable generateTileLayers(DigitEnum digitEnum, int tileId);
    public LayerDrawable alignTilesDigits(LayerDrawable generatedLayerDrawable, DigitEnum digitEnum);
    public void createTileButtons();
}
