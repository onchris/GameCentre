package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * The game selection screen where user chooses a game to play
 */
public class GameSelection extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    /**
     * TextView for showing current user's username
     */
    private TextView currentUserTextView;
    /**
     * The current user's username
     */
    private String currentUsername;
    /**
     * Current user's board list
     */
    private List<Board> boardList;
    /**
     * Current user's account
     */
    private Account currentAccount;
    /**
     * State of whether user is a guest.
     * State of whether user is at loading screen.
     */
    private boolean isGuest = false, atLoadGameScreen = false;
    /**
     * Custom scroll view for displaying list of games
     */
    private CustomScrollView gameListDisplay;
    /**
     * Custom adapter for displaying list of games by hooking up to CustomScrollView
     */
    private LoaderAdapter loaderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        currentUserTextView = findViewById(R.id.text_loggedas);
        if(!getIntent().getStringExtra("currentUser").equals("-1")) {
            currentUsername = getIntent().getStringExtra("currentUser");
            currentUserTextView.setText(currentUsername);
            getCurrentAccount(currentUsername);
        }
        else {
            currentUserTextView.setText("Guest");
            isGuest = true;
        }
        if(isGuest)
            boardList = new ArrayList<>();
        else
            boardList = currentAccount.getBoardList();
    }
    /**
     * Gets the account by iterating through the accounts file and set to currentAccount
     * @param accountName the account to search for
     */
    private void getCurrentAccount(String accountName){
        try {
            InputStream inputStream = this.openFileInput(LaunchCentre.ACCOUNTS_FILENAME);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                List<Account> accountList = (ArrayList<Account>) input.readObject();
                inputStream.close();
                for(Account account: accountList)
                {
                    if(account.getUsername().equals(currentUsername))
                        currentAccount = account;
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }
    /**
     * On click function for the new game button
     * @param v the current view(Called by application)
     */
    public void newGameButtonOnClick(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_sliding_difficulty);
        popup.show();
    }
    /**
     * Handles the function when a menu item is chosen
     * @param menuItem the menu item that user clicks on
     * @return whether the user successfully chose an item in the menu
     */
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.item1:
                return true;
            case R.id.item2:
                Board randomBoard = newRandomBoard(4,4);
                boardList.add(randomBoard);
                loaderAdapter.notifyDataSetChanged();
                LaunchCentre.makeCustomToastText(menuItem.toString(),getBaseContext());
                return true;
            case R.id.item3:
                return true;
            case R.id.item4:
                return true;
        }
        return false;
    }
    /**
     * Generates a new random board with sizes based on parameters passed in
     * @param rows the new game's row properties
     * @param columns the new game's columns properties
     * @return A random board with specified dimensions
     */
    private Board newRandomBoard(int rows, int columns){
        List<Tile> tiles = new ArrayList<>();
        int numTiles = rows * columns;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum));
        }
        Collections.shuffle(tiles);
        return new Board(tiles);
    }
    /**
     * On click function for SlidingTile game selection button
     * @param v the current view(Called by application)
     */
    public void slidingGameButtonOnClick(View v) {
        setContentView(R.layout.activity_loadedgamelist);
        gameListDisplay = findViewById(R.id.scrollable_loadablegames);
        loaderAdapter = new LoaderAdapter((ArrayList<Board>)boardList,gameListDisplay.getContext());
        gameListDisplay.setAdapter(loaderAdapter);
        atLoadGameScreen = true;
    }
    /**
     * On click function for SlidingTile game selection button
     * @param v the current view(Called by application)
     */
    public void resetOnClick(View v){
        boardList.clear();
        loaderAdapter.notifyDataSetChanged();
    }
    /**
     * Handles the functionality of the phone's back button
     * If at loading screen, back button would send user to the Game Selection screen
     * instead of login screen, vice versa.
     */
    @Override
    public void onBackPressed(){
        if(atLoadGameScreen) {
            atLoadGameScreen = false;
            setContentView(R.layout.activity_games);
        }
        else
            super.onBackPressed();
    }
    /**
     * Static function for saving a file for BoardManager
     * @param fileName directory and name of the file
     * @param bm the BoardManager to be saved
     * @param ctx the current context
     * @deprecated Might be deprecated if StartingActivity is no longer used
     */
    public static void saveToFile(String fileName, BoardManager bm, Context ctx) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream( ctx.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(bm);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
