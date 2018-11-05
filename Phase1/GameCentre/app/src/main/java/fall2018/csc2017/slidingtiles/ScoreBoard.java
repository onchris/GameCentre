package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static fall2018.csc2017.slidingtiles.UtilityManager.newRandomBoard;
import static fall2018.csc2017.slidingtiles.UtilityManager.saveBoardManagerToFile;

public class ScoreBoard extends AppCompatActivity{

    /**
     * Current user's account
     */
    private Account currentAccount;
    /**
     * Account list, to be loaded from local file
     */
    private List<Account> accountsList = new ArrayList<>();
    /**
     * Listview for showing the list of scores.
     */
    private ScoreManager scoreManager;
    private ListView scoreList;
    private List<String> displayUserScoresList = new ArrayList<>();
    private List<String> displayGameScoresList = new ArrayList<>();
    private TextView currentScore;
    private boolean IS_GUEST;
    private boolean IS_GLOBAL_SCOREBOARD;
    private Board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        scoreList = findViewById(R.id.scoreboard_list);
        addChangeScoreboardViewButton();
        addNewGameButtonListener();

        if (getIntent().getStringExtra("currentGame").equals("slidingTiles")) {
            scoreManager = new SlidingTilesScoreManager(getIntent().getStringExtra("currentUsername"),
                    scoreList.getContext(),
                    Integer.parseInt(getIntent().getStringExtra("currentScore")));
        }
        displayGameScoresList = scoreManager.getDisplayGameScoresList();
        displayUserScoresList = scoreManager.getDisplayUserScoresList();

        if(!getIntent().getStringExtra("currentUsername").equals("-1")) {
            IS_GUEST = false;
            board = (Board) getIntent().getSerializableExtra("board");
        } else {
            IS_GUEST = true;
        }

        currentScore = findViewById(R.id.lastscore);
        currentScore.setText(getIntent().getStringExtra("currentScore"));

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this,
                R.layout.activity_scorelist, displayGameScoresList);
        IS_GLOBAL_SCOREBOARD = true;
        scoreList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    /**
     * Adding a button so the user can switch between the game-wide scoreboard to per-user scoreboard
     */
    public void addChangeScoreboardViewButton(){
        Button changeScoreboardView = findViewById(R.id.button_new_game);
        changeScoreboardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
            }
        });
    }

    /**
     * On click function for the change scoreboard view button
     * @param v the current view(Called by application)
     */
    public void changeScoreboardViewOnClick(View v) {
        Button switchScoreboardView = findViewById(R.id.switchscoreboardview);
        scoreList = findViewById(R.id.scoreboard_list);
        if (IS_GLOBAL_SCOREBOARD) {
            ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_scorelist, displayUserScoresList);
            scoreList.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            IS_GLOBAL_SCOREBOARD = !IS_GLOBAL_SCOREBOARD;
            if (IS_GUEST) {
                Toast.makeText(scoreList.getContext(), "Guest has no saved scores!", Toast.LENGTH_SHORT).show();
                IS_GLOBAL_SCOREBOARD = !IS_GLOBAL_SCOREBOARD;
            }
        } else {
            ArrayAdapter arrayAdapter = new ArrayAdapter<>(this,
                    R.layout.activity_scorelist, displayGameScoresList);
            scoreList.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            IS_GLOBAL_SCOREBOARD = !IS_GLOBAL_SCOREBOARD;
        }
    }

    /**
     * A button to allow the user to begin a new game of what they just played
     */
    private void addNewGameButtonListener() {
        Button newGameButton = findViewById(R.id.button_new_game);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IS_GUEST){
                    saveBoardManagerToFile(UtilityManager.TEMP_SAVE_FILENAME,
                            new BoardManager(), v.getContext());
                    Intent tmp = new Intent(v.getContext(), GameActivity.class);
                    tmp.putExtra("account", -1);
                    startActivity(tmp);
                } else {
                    BoardManager bm = new BoardManager(newRandomBoard(board.numRows,board.numColumns));
                    saveBoardManagerToFile(UtilityManager.TEMP_SAVE_FILENAME, bm, v.getContext());
                    Intent tmp = new Intent(v.getContext(), GameActivity.class);
                    currentAccount.getBoardList().add(bm);
                    tmp.putExtra("account", currentAccount);
                    tmp.putExtra("boardList", currentAccount.getBoardList());
                    tmp.putExtra("boardIndex", currentAccount.getBoardList().indexOf(bm));
                    startActivity(tmp);
                }
                finish();
            }
        });
    }

    /**
     * On click function for the game selection button
     * @param v the current view(Called by application)
     */
    public void gameSelectionButtonOnClick(View v){
        this.getIntent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        onBackPressed();
    }

    /**
     * Handles phone's back button functionality
     * If at Scoreboard screen, back button would send user to the Game Selection screen
     * instead of login screen, vice versa.
     */
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

}
