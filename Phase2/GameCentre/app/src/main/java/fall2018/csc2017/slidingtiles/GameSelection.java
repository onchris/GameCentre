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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import fall2018.csc2017.slidingtiles.ObstacleDodger.TiltGameActivity;
import fall2018.csc2017.slidingtiles.ObstacleDodger.ObGameActivity;
import fall2018.csc2017.slidingtiles.UltimateTTT.UltimateTTTGameActivity;

import static fall2018.csc2017.slidingtiles.UtilityManager.ACCOUNTS_FILENAME;
import static fall2018.csc2017.slidingtiles.UtilityManager.alertDialogBuilder;
import static fall2018.csc2017.slidingtiles.UtilityManager.loadAccountList;
import static fall2018.csc2017.slidingtiles.UtilityManager.makeCustomToastText;
import static fall2018.csc2017.slidingtiles.UtilityManager.newRandomBoard;
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
    public List<BoardManager> boardList;
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
    private AccountManager accountManager;
    private DialogManager dialogManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        accountManager = new AccountManager(loadAccountList(this));
        currentUserTextView = findViewById(R.id.text_loggedas);
        if (!getIntent().getStringExtra("currentUser").equals("-1")) {
            currentUsername = getIntent().getStringExtra("currentUser");
            currentUserTextView.setText(currentUsername);
            currentAccount = accountManager.getAccountFromUsername(currentUsername);
            IS_GUEST = false;
        } else {
            currentUserTextView.setText(getString(R.string.ga_guest_user));
            IS_GUEST = true;
        }
        boardList = accountManager.getCurrentAccountBoardList(currentAccount, IS_GUEST);
        dialogManager = new DialogManager(this);
    }

    /**
     * On click function for the new game button
     *
     * @param v the current view(Called by application)
     */
    public void newGameButtonOnClick(View v) {
        dialogManager.createDialog(R.menu.menu_sliding_difficulty, v, this);
    }

    /**
     * Handles the function when a menu item is chosen
     *
     * @param menuItem the menu item that user clicks on
     * @return whether the user successfully chose an item in the menu
     */
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        dialogManager.setDialogLayout(R.layout.dialog_slidingdifficulty);
        dialogManager.setupComponents(boardList, loaderAdapter, currentAccount);
        return dialogManager.onMenuItemClick(menuItem);
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
            loaderAdapter = new LoaderAdapter((ArrayList<BoardManager>) boardList, this);
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
//            saveBoardManagerToFile(UtilityManager.TEMP_SAVE_FILENAME,new BoardManager(newRandomBoard(4,4)), this);
            Intent tmp = new Intent(this, UltimateTTTGameActivity.class);
            startActivity(tmp);
        } else {
            Intent tmp = new Intent(this, UltimateTTTGameActivity.class);
//            tmp.putExtra("currentUsername", currentUsername);
            tmp.putExtra("account", currentAccount);
//            if (currentAccount.getUltimateTTTList().size() != 0) {
//                tmp.putExtra("ultTTTBoardManagers", (Serializable) currentAccount.getUltimateTTTList());
//                tmp.putExtra("ultTTTBoardNum", currentAccount.getUltimateTTTList().size() - 1);
//            }
            startActivity(tmp);
//            setContentView(R.layout.activity_loadedgamelist);
//            gameListDisplay = findViewById(R.id.scrollable_loadablegames);
//            loaderAdapter = new LoaderAdapter((ArrayList<BoardManager>) boardList/*TODO: make this an ultTTTgamelist*/, this);
//            loaderAdapter.account = currentAccount;
//
//            gameListDisplay.setAdapter(loaderAdapter);
//            atLoadGameScreen = true;
        }
    }

    /**
     * On click function for Obstacle Dodger game selection button
     *
     * @param v the current view(Called by application)
     */
    public void obDodgerGameButtonOnClick(View v) {
        if (IS_GUEST) {
            Intent tmp = new Intent(this, ObGameActivity.class);
            startActivity(tmp);
        } else {
            Intent tmp = new Intent(this, ObGameActivity.class);
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
        titleText.setText(getString(R.string.gs_confirm_deletion));
        titleText.setPadding(10, 10, 10, 10);
        titleText.setTextSize(30);
        titleText.setGravity(Gravity.CENTER);
        ad.setCustomTitle(titleText);
        ad.show();
    }

    /**
     * Viewing scoreboard
     * @param v the view, called by activity
     */
    public void scoreboardOnClick(View v){
        Intent scoreboardIntent =  new Intent(this, GeneralScoreboard.class);
        scoreboardIntent.putExtra("username", currentUsername);
        startActivity(scoreboardIntent);
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
