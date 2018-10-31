package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class ScoreBoard extends AppCompatActivity{

    /**
     * Account data storage file
     */
    public static final String ACCOUNTS_FILENAME = "account_file.ser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        loadUsersFromFile(ACCOUNTS_FILENAME);
        addNewGameButtonListener();
        addGameSelectionButtonListener();

        //TODO: need to somehow get the list of scores to come up
        ListView scoreList = findViewById(R.id.scoreboard_list);
        // Instantiating an array list
        List<String> scores = new ArrayList<>();
        scores.add("sadfjsadlfkg");
        scores.add("sadjkgdlfa;gfs");

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                scores );
        scoreList.setAdapter(arrayAdapter);

    }

    private void loadUsersFromFile(String fileName) {
        try {
            InputStream inputStream = getApplicationContext().openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("making scoreboard", "Oops! File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("making scoreboard", "Oops! Cannot read file: " + e.toString());
        } /*catch (ClassNotFoundException e) {
            Log.e("making scoreboard", "Oops! File contained unexpected data type: " + e.toString());
        }*/
    }

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
        Intent tmp = new Intent(v.getContext(), LaunchCentre.class);
        startActivity(tmp);
    }

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

    }

    /**
     * Handles phone's back button functionality
     * If at Scoreboard screen, back button would send user to the Game Selection screen
     * instead of login screen, vice versa.
     */
    @Override
    public void onBackPressed(){
        setContentView(R.layout.activity_games);
    }

}
