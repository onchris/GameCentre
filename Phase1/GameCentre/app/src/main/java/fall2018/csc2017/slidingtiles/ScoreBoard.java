package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class ScoreBoard extends AppCompatActivity {

    public static final String ACCOUNTS_FILENAME = "account_file.ser";
    private List<Account> userAccounts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        loadUsersFromFile(ACCOUNTS_FILENAME);
        addStartButtonListener();
        addGameSelectionButtonListener();
    }

    //TODO: need to somehow get the list of scores to come up

    private void loadUsersFromFile(String fileName) {
        try {
            InputStream inputStream = getApplicationContext().openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                userAccounts = (ArrayList<Account>) input.readObject();
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

    private void addStartButtonListener() {
        Button newGameButton = findViewById(R.id.button_new_game);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
            }
        });
    }

    private void addGameSelectionButtonListener() {
        Button gameSelectionButton = findViewById(R.id.button_game_selection);
        gameSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_games);
            }
        });
    }
}
