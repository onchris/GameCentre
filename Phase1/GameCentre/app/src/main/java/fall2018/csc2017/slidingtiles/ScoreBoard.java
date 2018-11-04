package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreBoard extends AppCompatActivity{

    /**
     * Account data storage file
     */
    public static final String ACCOUNTS_FILENAME = "account_file.ser";
    /**
     * Current user's account
     */
    private Account currentAccount;
    /**
     * Account list, to be loaded from local file
     */
    private List<Account> accountsList = new ArrayList<>();
    /**
     * Litview for showing the list of scores.
     */
    private boolean IS_GUEST;
    private ListView scoreList;
    private List<Integer> userScores = new ArrayList<>();
    private List<Pair<Integer, String>> gameScores = new ArrayList<>();
    private TextView currentScore;
    private ToggleButton changeScoreboardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        loadUsersFromFile(ACCOUNTS_FILENAME);
        addNewGameButtonListener();
        addGameSelectionButtonListener();

        if(!getIntent().getStringExtra("currentUsername").equals("-1")) {
            IS_GUEST = false;
            for(Account account: accountsList) {
                if(account.getUsername().equals(getIntent().getStringExtra("currentUsername"))) {
                    currentAccount = account;
                    userScores = currentAccount.getSlidingGameScores();
                    break;
                }
            }

        } else {
            IS_GUEST = true;
        }

        buildGameScoresList();

        currentScore = findViewById(R.id.lastscore);
        currentScore.setText(getIntent().getStringExtra("currentScore"));

        scoreList = findViewById(R.id.scoreboard_list);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this,
                R.layout.activity_scorelist, gameScores);
        scoreList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

    }

    /**
     * Load list of accounts to accountsList
     * @param fileName the name of the file
     */
    private void loadUsersFromFile(String fileName) {
        try {
            InputStream inputStream = getApplicationContext().openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                accountsList = (ArrayList<Account>) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("making scoreboard", "Oops! File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("making scoreboard", "Oops! Cannot read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("making scoreboard", "Oops! File contained unexpected data type: " + e.toString());
        }
    }

    private void buildGameScoresList() {
        Pair<Integer, String> p;
        for(Account account: accountsList) {
            List<Integer> accountScores = account.getSlidingGameScores();
            for (int i = 0; i <= accountScores.size() - 1; i ++) {
                p = new Pair<>(accountScores.get(i), account.getUsername());
                gameScores.add(p);
            }
        }
        if (IS_GUEST) {
            try {
                p = new Pair<>(Integer.parseInt(getIntent().getStringExtra("currentScore")), "Guest");
                gameScores.add(p);
            } catch (NumberFormatException e) {
                p = new Pair<>(-1, "Guest");
            }
        }
        //TODO: sort: Collections.sort();
    }

    public void onCheckedChangeScoreboardView(CompoundButton buttonView, boolean isChecked) {
        ToggleButton newGameButton = findViewById(R.id.switchscoreboardview);
        scoreList = findViewById(R.id.scoreboard_list);
        if (isChecked) {
            ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_scorelist, userScores);
            scoreList.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            if (IS_GUEST) {
                Toast.makeText(scoreList.getContext(), "Guest has no saved scores!", Toast.LENGTH_SHORT).show();
            }
        } else {
            ArrayAdapter arrayAdapter = new ArrayAdapter<>(this,
                    R.layout.activity_scorelist, gameScores);
            scoreList.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Allows user to begin a new game of what they just played
     */
    private void addNewGameButtonListener() {
        Button newGameButton = findViewById(R.id.button_new_game);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
            }
        });
    }

    /**
     * On click function for the new game button
     * @param v the current view(Called by application)
     */
    public void newGameButtonOnClick(View v){
        Intent tmp = new Intent(v.getContext(), GameActivity.class);
        startActivity(tmp);
    }

    /**
     * Allow user to go back to the list of games to pick a different game
     */
    private void addGameSelectionButtonListener() {
        Button gameSelectionButton = findViewById(R.id.button_game_selection);
        gameSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_launchcentre);
            }
        });
    }

    /**
     * On click function for the game selection button
     * @param v the current view(Called by application)
     */
    public void gameSelectionButtonOnClick(View v){
        Intent tmp = new Intent(v.getContext(), GameSelection.class);
        if (!IS_GUEST) {
            tmp.putExtra("currentUser", currentAccount.getUsername());
        } else {
            tmp.putExtra("currentUser", "-1");
        }
        startActivity(tmp);
    }

    /**
     * Handles phone's back button functionality
     * If at Scoreboard screen, back button would send user to the Game Selection screen
     * instead of login screen, vice versa.
     */
    @Override
    public void onBackPressed(){
        Intent tmp = new Intent(scoreList.getContext(), GameSelection.class);
        if (!IS_GUEST) {
            tmp.putExtra("currentUser", currentAccount.getUsername());
        } else {
            tmp.putExtra("currentUser", "-1");
        }
        startActivity(tmp);
    }

}
