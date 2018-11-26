package fall2018.csc2017.slidingtiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardSetup {
    public static BoardManager setUp4x4Unsolved(){
        BoardManager bm = new BoardManager();
        bm = ensureUnsolved(bm);
        return bm;
    }
    public static BoardManager setUp4x4Solved(){
        return new BoardManager(new Board(setupCustomBoard(4,4)));
    }

    private static List<Tile> setupCustomBoard(int row, int column){
        List<Tile> tiles = new ArrayList<>();
        int numTiles = row * column;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum));
        }
        return tiles;
    }
    private static BoardManager ensureUnsolved(BoardManager bm){
        while(bm.puzzleSolved()){
            bm.getBoard().swapTiles(0,0,0,1);
        }
        return bm;
    }

}
