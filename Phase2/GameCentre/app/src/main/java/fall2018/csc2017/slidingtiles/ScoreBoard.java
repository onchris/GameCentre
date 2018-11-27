package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fall2018.csc2017.slidingtiles.ObstacleDodger.ObGameActivity;

import static fall2018.csc2017.slidingtiles.UtilityManager.newRandomBoard;
import static fall2018.csc2017.slidingtiles.UtilityManager.saveBoardManagerToFile;

/**
 * A general scoreboard activity that displays game-wide and user-specific scores.
 */
public class ScoreBoard extends AppCompatActivity{

    /**
     * Current user's account
     */
    private Account currentAccount;
    /**
     * ScoreManager to manage the scores for display.
     */
    private ScoreManager scoreManager;
    /**
     * ListView for the scores to be displayed
     */
    private ListView scoreList;
    /**
     * Scores list for user scores for display with user's name
     */
    private List<String> displayUserScoresList = new ArrayList<>();
    /**
     * Scores List for Game-wide scores for display with all pair
     */
    private List<String> displayGameScoresList = new ArrayList<>();
    /**
     * A check for if the player is a guest
     */
    private boolean IS_GUEST;
    /**
     * A check for if the scoreboard is global
     */
    private boolean IS_GLOBAL_SCOREBOARD;
    /**
     * The board for the game
     */
    private Board board;
    private String currentGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        scoreList = findViewById(R.id.scoreboard_list);

        currentGame = getIntent().getStringExtra("currentGame");

        addChangeScoreboardViewButton();
        addNewGameButtonListener();

        if (currentGame.equals("slidingTiles")) {
            scoreManager = new SlidingTilesScoreManager(getIntent().getStringExtra("currentUsername"),
                    scoreList.getContext(),
                    Integer.parseInt(getIntent().getStringExtra("currentScore")));
        } else if (currentGame.equals("obDodger")) {
            scoreManager = new ObDodgerScoreManager(getIntent().getStringExtra("currentUsername"), //TODO: make this for obstacledodger
                    scoreList.getContext(),
                    Integer.parseInt(getIntent().getStringExtra("currentScore")));
        }
        displayGameScoresList = scoreManager.getDisplayGameScoresList();
        displayUserScoresList = scoreManager.getDisplayUserScoresList();

        if(scoreManager.getCurrentAccount() != null) {
            IS_GUEST = false;
            currentAccount = scoreManager.getCurrentAccount();
            if (getIntent().hasExtra("board")) { //TODO: board???
                board = (Board) getIntent().getSerializableExtra("board");
            }
        }

        /*
          TextView for the score from the most recently completed game
         */
        TextView currentScore = findViewById(R.id.lastscore);
        currentScore.setText(getIntent().getStringExtra("currentScore"));

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this,
                R.layout.activity_scorelist, displayGameScoresList);
        IS_GLOBAL_SCOREBOARD = true;
        scoreList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    /**
     * A button to allow the user can switch between the game-wide scoreboard to per-user scoreboard
     */
    public void addChangeScoreboardViewButton(){
        Button changeScoreboardView = findViewById(R.id.button_new_game);
        changeScoreboardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_score_board);
            }
        });
    }

    /**
     * On click function for the change scoreboard view button
     * @param v the current view(Called by application)
     */
    public void changeScoreboardViewOnClick(View v) {
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
        if (currentGame.equals("slidingTiles")) {
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
                        BoardManager bm = new BoardManager(newRandomBoard(board.getNumRows(), board.getNumColumns()));
                        saveBoardManagerToFile(UtilityManager.TEMP_SAVE_FILENAME, bm, v.getContext());
                        Intent tmp = new Intent(v.getContext(), GameActivity.class);
                        currentAccount.getBoardList().add(bm);
                        tmp.putExtra("account", currentAccount);
                        tmp.putExtra("boardList", (Serializable) currentAccount.getBoardList());
                        tmp.putExtra("boardIndex", currentAccount.getBoardList().indexOf(bm));
                        startActivity(tmp);
                    }
                    finish();
                }
            });
        } else if (currentGame.equals("obDodger")) {
            Button newGameButton = findViewById(R.id.button_new_game);
            newGameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(IS_GUEST){
                        Intent tmp = new Intent(v.getContext(), ObGameActivity.class);
                        startActivity(tmp);
                    } else {
                        Intent tmp = new Intent(v.getContext(), ObGameActivity.class);
                        tmp.putExtra("account", currentAccount);
                        startActivity(tmp);
                    }
                    finish();
                }
            });
        }
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
