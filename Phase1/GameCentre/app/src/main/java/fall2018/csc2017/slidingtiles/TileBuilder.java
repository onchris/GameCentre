package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.Nullable;
import android.widget.Button;

import java.util.ArrayList;

public class TileBuilder implements Tileable{
    private final BoardManager boardManager;
    private final int rows, columns, columnWidth;
    private final ArrayList<Button> tileButtons;
    private Context currentContext;
    private boolean useImages;
    private ArrayList<Bitmap> imageSet;
    public TileBuilder(BoardManager boardManager, Context currentContext, int columnWidth){
        this.boardManager = boardManager;
        this.tileButtons = new ArrayList<>();
        this.currentContext = currentContext;
        this.rows = boardManager.getBoard().numRows;
        this.columns = boardManager.getBoard().numColumns;
        this.columnWidth = columnWidth;
    }
    public void setUseImages(boolean useImages, ArrayList<Bitmap> imageSet){
        this.useImages = useImages;
        this.imageSet = imageSet;
    }
    public ArrayList<Button> getTileButtons(){
        return tileButtons;
    }
    @Override
    public Drawable generateBackground(boolean isBlank, Tile tile) {
        int tileId = tile.getId();
        if(isBlank)
        {
            if(!tile.hasBackground())
                tile.setBackground(currentContext.getDrawable(R.drawable.bg_simplebg));
            return currentContext.getDrawable(R.drawable.bg_simplebg);
        }
        else if(tileId < 10){
            LayerDrawable ld = generateTileLayers(DigitEnum.DIGIT_ONES, tileId);
            if(!tile.hasBackground())
                tile.setBackground(ld);
            return ld;
        }
        else if (tileId < 100){
            LayerDrawable ld = generateTileLayers(DigitEnum.DIGIT_TENS, tileId);
            if(!tile.hasBackground())
                tile.setBackground(ld);
            return ld;
        } else if (tileId < 1000)
        {
            LayerDrawable ld = generateTileLayers(DigitEnum.DIGIT_THOU, tileId);
            if(!tile.hasBackground())
                tile.setBackground(ld);
            return ld;
        }
        return null;
    }

    @Override
    public Drawable generateImageBackground(boolean isBlank, Tile tile, @Nullable Bitmap imageBitmap) {
        if(isBlank)
        {
            if(!tile.hasBackground())
                tile.setBackground(currentContext.getDrawable(R.drawable.bg_simplebg));
            return currentContext.getDrawable(R.drawable.bg_simplebg);
        } else {
            Drawable bg = new BitmapDrawable(currentContext.getResources(), imageBitmap);
            if (!tile.hasBackground())
                tile.setBackground(bg);
            return bg;
        }
    }

    @Override
    public LayerDrawable generateTileLayers(DigitEnum digitEnum, int tileId) {
        Drawable bg = currentContext.getDrawable(R.drawable.bg_simplebg);
        String thouString, tensString, onesString;
        LayerDrawable ld;
        int onesNumberPath, tensNumberPath, thouNumberPath;
        switch (digitEnum) {
            case DIGIT_ONES:
                onesNumberPath = currentContext.getResources()
                        .getIdentifier("ic_"+Integer.toString(tileId),
                                "drawable", currentContext.getPackageName());
                return alignTilesDigits(new LayerDrawable(new Drawable[]
                                {bg,currentContext.getDrawable(onesNumberPath)}),
                        digitEnum);
            case DIGIT_TENS:
                tensString = Integer.toString(tileId).substring(0,1);
                onesString = Integer.toString(tileId).substring(1);
                onesNumberPath = currentContext.getResources()
                        .getIdentifier("ic_"+onesString, "drawable",
                                currentContext.getPackageName());
                tensNumberPath = currentContext.getResources()
                        .getIdentifier("ic_"+tensString, "drawable",
                                currentContext.getPackageName());
                return alignTilesDigits(new LayerDrawable(new Drawable[]
                                {bg,currentContext.getDrawable(tensNumberPath),
                                        currentContext.getDrawable(onesNumberPath)}),
                        digitEnum);
            case DIGIT_THOU:
                thouString = Integer.toString(tileId).substring(0,1);
                tensString = Integer.toString(tileId).substring(1,2);
                onesString = Integer.toString(tileId).substring(2);
                thouNumberPath = currentContext.getResources()
                        .getIdentifier("ic_"+thouString, "drawable",
                                currentContext.getPackageName());
                tensNumberPath = currentContext.getResources()
                        .getIdentifier("ic_"+tensString, "drawable",
                                currentContext.getPackageName());
                onesNumberPath = currentContext.getResources()
                        .getIdentifier("ic_"+onesString, "drawable",
                                currentContext.getPackageName());
                return alignTilesDigits(new LayerDrawable(new Drawable[]
                                {bg,currentContext.getDrawable(thouNumberPath),
                                        currentContext.getDrawable(tensNumberPath),
                                        currentContext.getDrawable(onesNumberPath)}),
                        digitEnum);
        }
        return null;
    }

    @Override
    public LayerDrawable alignTilesDigits(LayerDrawable generatedLayerDrawable, DigitEnum digitEnum) {
        if( digitEnum == DigitEnum.DIGIT_ONES) {
            generatedLayerDrawable.setLayerInset(1, columnWidth / 4, 0, 0, 30);
            generatedLayerDrawable.setLayerWidth(1, columnWidth / 2);
            return generatedLayerDrawable;
        }
        for(int i = 1; i < generatedLayerDrawable.getNumberOfLayers(); i++)
        {
            if(digitEnum == DigitEnum.DIGIT_TENS) {
                generatedLayerDrawable.setLayerWidth(i, columnWidth / 2 - 5);
                generatedLayerDrawable.setLayerInset(i, i == 1 ? 0 : columnWidth/2, 0, 0, 30);
            }
            else if (digitEnum == DigitEnum.DIGIT_THOU){
                generatedLayerDrawable.setLayerWidth(i, columnWidth / 3 - 5);
                generatedLayerDrawable.setLayerInset(i, i == 1 ? 0 : (i-1)*columnWidth/3, 0, 0, 30);
            }
        }
        return generatedLayerDrawable;
    }

    @Override
    public void createTileButtons() {
        Board board = boardManager.getBoard();
        ArrayList<Bitmap> imageSet = useImages ? this.imageSet: null;
        for (int row = 0; row != rows; row++) {
            for (int col = 0; col != columns; col++) {
                Button tmp = new Button(currentContext);
                Tile tile = board.getTile(row,col);
                int tileId = board.getTile(row,col).getId();
                if(useImages){
                    if(board.getTile(row,col).getId() == columns * rows)
                        tmp.setBackground(generateImageBackground(true, tile, null));
                    tmp.setBackground(generateImageBackground(false, tile, imageSet.get(tileId-1)));
                }
                else if(board.getTile(row,col).getId() < columns * rows)
                    tmp.setBackground(generateBackground(false, tile));
                else
                    tmp.setBackground(generateBackground(true, tile));
                this.tileButtons.add(tmp);
            }
        }
    }
}
