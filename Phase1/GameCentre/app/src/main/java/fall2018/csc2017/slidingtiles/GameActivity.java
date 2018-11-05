package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import static fall2018.csc2017.slidingtiles.UtilityManager.TEMP_SAVE_FILENAME;
import static fall2018.csc2017.slidingtiles.UtilityManager.makeCustomToastText;
import static fall2018.csc2017.slidingtiles.UtilityManager.saveBoardsToAccounts;
/**
 * The game activity.
 */
public class GameActivity extends AppCompatActivity implements Observer {
    /**
     * The board manager.
     */
    private BoardManager boardManager;
    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;
    /**
     * The current user's username
     */
    private String currentUsername;

    /**
     * Constants for swiping directions. Should be an enum, probably.
     */
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    public static final int SAVE_INTERVAL = 10000;

    // Grid View and calculated column height and width based on device size
    private GestureDetectGridView gridView;
    private static int columnWidth, columnHeight;
    private Account currentAccount;
    private ArrayList<BoardManager> boardList;
    private int boardIndex;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Button undoButton;
    private Integer currentScore;

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    // Display
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
        //if (boardManager.puzzleSolved()) {
            currentScore = -100;//TODO: remove, it's not a real score
            gridView = findViewById(R.id.grid);
            Intent tmp = new Intent(gridView.getContext(), ScoreBoard.class);
            if(!GameSelection.IS_GUEST)
                tmp.putExtra("currentUsername", currentAccount.getUsername());
            else
                tmp.putExtra("currentUsername", "-1");
            tmp.putExtra("currentScore", currentScore.toString()); //TODO: pass the current score
            startActivity(tmp);
        //}
        undoButton = findViewById(R.id.UndoButton);
        undoButton.setText("Undo:"+boardManager.getNumCanUndo());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFromFile(TEMP_SAVE_FILENAME);
        createTileButtons(this);
        currentAccount = (Account) getIntent().getSerializableExtra("account");
        boardList = (ArrayList<BoardManager>) getIntent().getSerializableExtra("boardList");
        boardIndex = this.getIntent().getIntExtra("boardIndex", -1);
        setContentView(R.layout.activity_main);
        addUndoButtonListener();
        TextView v = findViewById(R.id.text_currentUserGame);
        if(!GameSelection.IS_GUEST)
            v.setText(currentAccount.getUsername());
        else
            v.setText("Guest");

        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(Board.NUM_COLS);
        gridView.setBoardManager(boardManager);
        boardManager.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / Board.NUM_COLS;
                        columnHeight = displayHeight / Board.NUM_ROWS;
                        display();
                    }
                });

        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onClickSaveBoard(getCurrentFocus(),true);
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, SAVE_INTERVAL, SAVE_INTERVAL);
    }
    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        Board board = boardManager.getBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != Board.NUM_ROWS; row++) {
            for (int col = 0; col != Board.NUM_COLS; col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(board.getTile(row, col).getBackground());
                this.tileButtons.add(tmp);
            }
        }
    }
    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        Board board = boardManager.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / Board.NUM_ROWS;
            int col = nextPos % Board.NUM_COLS;
            b.setBackgroundResource(board.getTile(row, col).getBackground());
            nextPos++;
        }
    }
    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(StartingActivity.TEMP_SAVE_FILENAME);
    }
    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {

        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                boardManager = (BoardManager) input.readObject();
                inputStream.close();
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
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(boardManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    /**
     * Activate the undo button.
     */
    private void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.UndoButton);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boardManager.canUndo()){
                    boardManager.undo();
                    display();
                } else{
                    Context context = getApplicationContext();
                    Toast.makeText(context, "Not Able To Undo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onClickSaveBoard(View v, boolean isAutosave){
        if(GameSelection.IS_GUEST)
            makeCustomToastText("Cannot save as guest!", this);
        else {
            if (boardIndex != -1) {
                boardList.set(boardIndex, boardManager);
            }
            saveBoardsToAccounts(this, currentAccount, boardList);
            if (isAutosave)
                makeCustomToastText("Auto-saved!", this);
        }
    }
    public void onClickSaveBoard(View v){
        if(GameSelection.IS_GUEST)
            makeCustomToastText("Cannot save as guest!", this);
        else {
            if (boardIndex != -1) {
                boardList.set(boardIndex, boardManager);
            }
            saveBoardsToAccounts(this, currentAccount, boardList);
            makeCustomToastText("Saved!", this);
        }
    }
    @Override
    public void onBackPressed() {
        onClickSaveBoard(getCurrentFocus(), false);
        super.onBackPressed();
        timer.cancel();
        timerTask.cancel();
        finish();
    }
    @Override
    public void update(Observable o, Object arg) {
        display();
    }
}
