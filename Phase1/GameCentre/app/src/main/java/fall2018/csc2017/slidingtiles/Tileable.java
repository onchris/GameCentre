package fall2018.csc2017.slidingtiles;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public interface Tileable {
    enum DigitEnum
    {
        DIGIT_ONES, DIGIT_TENS, DIGIT_THOU
    }

    /**
     * Generates a normal tile's background
     * @param isBlank whether or not the tile should be blank
     * @param tile the tile to be generated.
     * @return A drawable where buttons can set their background to
     */
    Drawable generateBackground(boolean isBlank, Tile tile);

    /**
     * Generates a image tile's background
     * @param isBlank whether or not the tile should be blank
     * @param tile the tile to be generated.
     * @param imageBitmap the image to set the tile with
     * @return A drawable where buttons can set their background to
     */
    Drawable generateImageBackground(boolean isBlank, Tile tile, @Nullable Bitmap imageBitmap);

    /**
     * Generates layers of drawables including background and digits
     * @param digitEnum enum for digits to generate
     * @param tileId the tile number to be genrated.
     * @return A LayerDrawable where buttons can set their background to
     */
    LayerDrawable generateTileLayers(DigitEnum digitEnum, int tileId);

    /**
     * Aligns digits in their correct position
     * @param generatedLayerDrawable the compiled drawables
     * @param digitEnum enum for digits to generate
     * @return A LayerDrawable where buttons can set their background to
     */
    LayerDrawable alignTilesDigits(LayerDrawable generatedLayerDrawable, DigitEnum digitEnum);

    /**
     * Builds the tileButtons
     */
    void createTileButtons();
}
