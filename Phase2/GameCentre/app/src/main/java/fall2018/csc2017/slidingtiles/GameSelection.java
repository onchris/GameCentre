package fall2018.csc2017.slidingtiles;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fall2018.csc2017.slidingtiles.ObstacleDodger.TiltGameActivity;
import fall2018.csc2017.slidingtiles.UltimateTTT.UltimateTTTGameActivity;

import static fall2018.csc2017.slidingtiles.UtilityManager.ACCOUNTS_FILENAME;
import static fall2018.csc2017.slidingtiles.UtilityManager.alertDialogBuilder;
import static fall2018.csc2017.slidingtiles.UtilityManager.makeCustomToastText;
import static fall2018.csc2017.slidingtiles.UtilityManager.saveBoardManagerToFile;
import static fall2018.csc2017.slidingtiles.UtilityManager.saveBoardsToAccounts;

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
    public ArrayList<BoardManager> boardList;
    /**
     * Current user's account
     */
    private Account currentAccount;
    /**
     * State of whether user is a guest.
     */
    public static boolean IS_GUEST = false;
    /**
     * State of whether user is at loading screen.
     */
    private boolean atLoadGameScreen = false;
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
        if (!getIntent().getStringExtra("currentUser").equals("-1")) {
            currentUsername = getIntent().getStringExtra("currentUser");
            currentUserTextView.setText(currentUsername);
            currentAccount = getCurrentAccount(currentUsername);
            IS_GUEST = false;
        } else {
            currentUserTextView.setText("Guest");
            IS_GUEST = true;
        }
        getCurrentAccountBoardList();
    }

    /**
     * Gets the current accounts' board list
     */
    private void getCurrentAccountBoardList() {
        if (IS_GUEST) {
            boardList = new ArrayList<>();
        } else {
            boardList = currentAccount.getBoardList();
        }
    }

    /**
     * Gets the account by iterating through the accounts file and set to currentAccount
     *
     * @param accountName the account to search for
     */
    private Account getCurrentAccount(String accountName) {
        try {
            InputStream inputStream = this.openFileInput(ACCOUNTS_FILENAME);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                List<Account> accountList = (ArrayList<Account>) input.readObject();
                inputStream.close();
                for (Account account : accountList) {
                    if (account.getUsername().equals(accountName))
                        return account;
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
        return null;
    }

    /**
     * On click function for the new game button
     *
     * @param v the current view(Called by application)
     */
    public void newGameButtonOnClick(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_sliding_difficulty);
        popup.show();
    }

    /**
     * Handles the function when a menu item is chosen
     *
     * @param menuItem the menu item that user clicks on
     * @return whether the user successfully chose an item in the menu
     */
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_slidingdifficulty, null));
        final Dialog dialog = builder.create();
        switch (menuItem.getItemId()) {
            case R.id.item1:
                Board randomBoard3 = newRandomBoard(3, 3);
                boardList.add(new BoardManager(randomBoard3));
                loaderAdapter.notifyDataSetChanged();
                saveBoardsToAccounts(this, currentAccount, boardList);
                makeCustomToastText(menuItem.toString(), getBaseContext());
                return true;
            case R.id.item2:
                Board randomBoard4 = newRandomBoard(4, 4);
                boardList.add(new BoardManager(randomBoard4));
                loaderAdapter.notifyDataSetChanged();
                saveBoardsToAccounts(this, currentAccount, boardList);
                makeCustomToastText(menuItem.toString(), getBaseContext());
                return true;
            case R.id.item3:
                Board randomBoard5 = newRandomBoard(5, 5);
                boardList.add(new BoardManager(randomBoard5));
                loaderAdapter.notifyDataSetChanged();
                saveBoardsToAccounts(this, currentAccount, boardList);
                makeCustomToastText(menuItem.toString(), getBaseContext());
                return true;
            case R.id.item4:
                dialog.show();
                Button confirmButton = dialog.findViewById(R.id.button_confirm_difficulty);
                Button loadImageButton = dialog.findViewById(R.id.button_loadImage);
                final ImageView imagePreview = dialog.findViewById(R.id.iv_preview);
                final EditText rows = dialog.findViewById(R.id.text_row);
                final EditText columns = dialog.findViewById(R.id.text_column);
                final EditText undos = dialog.findViewById(R.id.text_undos);
                final EditText etUrl = dialog.findViewById(R.id.et_Url);
                final ImageResultReceiver resultReceiver = new ImageResultReceiver(new Handler(), imagePreview);
                resultReceiver.setReceiver(resultReceiver);
                loadImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows.getText().toString().equals("") ||
                                columns.getText().toString().equals("") ||
                                undos.getText().toString().equals("")) {
                            makeCustomToastText("Fields must not be empty!", v.getContext());
                        } else if (Integer.parseInt(rows.getText().toString()) < 3 ||
                                Integer.parseInt(columns.getText().toString()) < 3) {
                            makeCustomToastText("Rows/Columns cannot be lesser than 3!", v.getContext());
                        } else if (Integer.parseInt(rows.getText().toString()) > 31 ||
                                Integer.parseInt(columns.getText().toString()) > 31) {
                            makeCustomToastText("Dude stop you can't even see the board at this size", v.getContext());
                        } else {
                            String url = etUrl.getText().toString();
                            Intent imageIntent = new Intent(v.getContext(), ImageServiceIntent.class);
                            imageIntent.putExtra("receiver", resultReceiver);
                            imageIntent.putExtra("url", url);
                            imageIntent.putExtra("rows", Integer.parseInt(rows.getText().toString()));
                            imageIntent.putExtra("columns", Integer.parseInt(columns.getText().toString()));
                            startService(imageIntent);
                        }
                    }
                });
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckBox useImage = dialog.findViewById(R.id.cb_useImage);
                        if (useImage.isChecked()) {
                            if (resultReceiver.contentReceived()) {
                                Board randomBoard = newRandomBoard(resultReceiver.getRow(), resultReceiver.getCol());
                                BoardManager bm = new BoardManager(randomBoard);
                                bm.setCustomImageSet(resultReceiver.getBitmapArrayList());
                                bm.setUseImage(true);
                                bm.setNumCanUndo(Integer.parseInt(undos.getText().toString()));
                                boardList.add(bm);
                                loaderAdapter.notifyDataSetChanged();
                                saveBoardsToAccounts(getBaseContext(), currentAccount, boardList);
                                makeCustomToastText(randomBoard.getTilesDimension(), getBaseContext());
                                dialog.dismiss();
                            } else if (resultReceiver.invalidImageLink()) {
                                makeCustomToastText("Invalid image link, make sure to copy image link address directly!", view.getContext());
                            } else {
                                makeCustomToastText("You must wait for your image to finish downloading!", view.getContext());
                            }
                        } else if (rows.getText().toString().equals("") ||
                                columns.getText().toString().equals("") ||
                                undos.getText().toString().equals("")) {
                            makeCustomToastText("Fields must not be empty!", view.getContext());
                        } else if (Integer.parseInt(rows.getText().toString()) < 3 ||
                                Integer.parseInt(columns.getText().toString()) < 3) {
                            makeCustomToastText("Rows/Columns cannot be lesser than 3!", view.getContext());
                        } else if (Integer.parseInt(rows.getText().toString()) > 31 ||
                                Integer.parseInt(columns.getText().toString()) > 31) {
                            makeCustomToastText("Dude stop you can't even see the board at this size", view.getContext());
                        } else {
                            Board randomBoard = newRandomBoard(Integer.parseInt(rows.getText().toString()),
                                    Integer.parseInt(columns.getText().toString()));
                            BoardManager bm = new BoardManager(randomBoard);
                            bm.setNumCanUndo(Integer.parseInt(undos.getText().toString()));
                            boardList.add(bm);
                            loaderAdapter.notifyDataSetChanged();
                            saveBoardsToAccounts(getBaseContext(), currentAccount, boardList);
                            makeCustomToastText(randomBoard.getTilesDimension(), getBaseContext());
                            dialog.dismiss();
                        }
                    }
                });
                return true;
        }
        return false;
    }

    /**
     * Generates a new random board with sizes based on parameters passed in
     *
     * @param rows    the new game's row properties
     * @param columns the new game's columns properties
     * @return A random board with specified dimensions
     */
    private Board newRandomBoard(int rows, int columns) {
        List<Tile> tiles = new ArrayList<>();
        int numTiles = rows * columns;
        int checkSolvable = 0; int count = 0;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum));
        }
        Collections.shuffle(tiles);
        while(true){
            for (int tileNum = 0; tileNum < numTiles; tileNum++) {
                if(tiles.get(tileNum).getId() == numTiles){continue;}
                for (int x = tileNum + 1; x < numTiles; x++) {
                    if(tiles.get(tileNum).getId() == numTiles){continue;}
                    if(tiles.get(tileNum).getId() > tiles.get(x).getId()){
                        count++;
                    }
                }
                checkSolvable += count;
                count = 0;
            }
            if(checkSolvable % 2 == 0){
                break;
            }else {Collections.shuffle(tiles); checkSolvable = 0;}
        }
        Board b = new Board(tiles, rows, columns);
        return b;
    }

    /**
     * On click function for SlidingTile game selection button
     *
     * @param v the current view(Called by application)
     */
    public void slidingGameButtonOnClick(View v) {
        if (IS_GUEST) {
            saveBoardManagerToFile(UtilityManager.TEMP_SAVE_FILENAME,
                    new BoardManager(newRandomBoard(4, 4)), this);
            Intent tmp = new Intent(this, GameActivity.class);
            this.startActivity(tmp);
        } else {
            setContentView(R.layout.activity_loadedgamelist);
            gameListDisplay = findViewById(R.id.scrollable_loadablegames);
            loaderAdapter = new LoaderAdapter(boardList, this);
            loaderAdapter.account = currentAccount;

            gameListDisplay.setAdapter(loaderAdapter);
            atLoadGameScreen = true;
        }
    }

    /**
     * On click function for Ultimate Tic Tac Toe game selection button
     *
     * @param v the current view(Called by application)
     */
    public void ultTTTGameButtonOnClick(View v) {
        if (IS_GUEST) {
            //saveBoardManagerToFile(UtilityManager.TEMP_SAVE_FILENAME,new BoardManager(newRandomBoard(4,4)), this);
            Intent tmp = new Intent(this, UltimateTTTGameActivity.class);
            startActivity(tmp);
        } else {
            setContentView(R.layout.activity_loadedgamelist);
            gameListDisplay = findViewById(R.id.scrollable_loadablegames);
            loaderAdapter = new LoaderAdapter(boardList/*TODO: make this an ultTTTgamelist*/, this);
            loaderAdapter.account = currentAccount;

            gameListDisplay.setAdapter(loaderAdapter);
            atLoadGameScreen = true;
        }
    }

    /**
     * On click function for Obstacle Dodger game selection button
     *
     * @param v the current view(Called by application)
     */
    public void obDodgerGameButtonOnClick(View v) {
        if (IS_GUEST) {
            Intent tmp = new Intent(this, TiltGameActivity.class);
            startActivity(tmp);
        } else {
            Intent tmp = new Intent(this, TiltGameActivity.class);
            tmp.putExtra("account", currentAccount);
            startActivity(tmp);
        }
    }


    /**
     * On click function for SlidingTile game selection button
     *
     * @param v the current view(Called by application)
     */
    public void resetOnClick(View v) {
        final Context ctx = this;
        AlertDialog ad = alertDialogBuilder(null, "There is no going back, son", ctx);
        ad.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boardList.clear();
                loaderAdapter.notifyDataSetChanged();
                saveBoardsToAccounts(ctx, currentAccount, boardList);
            }
        });
        ad.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        TextView titleText = new TextView(this);
        titleText.setText("Delete all games?");
        titleText.setPadding(10, 10, 10, 10);
        titleText.setTextSize(30);
        titleText.setGravity(Gravity.CENTER);
        ad.setCustomTitle(titleText);
        ad.show();
    }

    /**
     * Handles the functionality of the phone's back button
     * If at loading screen, back button would send user to the Game Selection screen
     * instead of login screen, vice versa.
     */
    @Override
    public void onBackPressed() {
        if (atLoadGameScreen) {
            atLoadGameScreen = false;
            setContentView(R.layout.activity_games);
            currentUserTextView = findViewById(R.id.text_loggedas);
            currentUserTextView.setText(currentUsername);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
