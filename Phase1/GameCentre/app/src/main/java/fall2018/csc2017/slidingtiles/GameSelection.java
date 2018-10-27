package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

public class GameSelection extends AppCompatActivity {
    private TextView currentUserTextView;
    private String currentUsername;
    private List<Board> boardList;
    private Account currentAccount;
    private boolean isGuest = false;
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
        addSlidingGameButtonListener();
    }

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

    private void addSlidingGameButtonListener() {
        ImageButton game1Button = findViewById(R.id.button_gameselect1);
        game1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent tmp = new Intent(v.getContext(), StartingActivity.class);
//                startActivity(tmp);
                setContentView(R.layout.activity_loadedgamelist);
                addDebugListener();
            }
        });
    }
    private void addDebugListener(){
        Button debugButton = findViewById(R.id.button_debug);
        debugButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                for(int i = 0; i < 10; i++) {
                    List<Tile> tiles = new ArrayList<>();
                    final int numTiles = Board.NUM_ROWS * Board.NUM_COLS;
                    for (int tileNum = 0; tileNum != numTiles; tileNum++) {
                        tiles.add(new Tile(tileNum));
                    }
                    Collections.shuffle(tiles);
                    boardList.add(new Board(tiles));
                }
                //currentAccount.setBoardList(boardList);
                LaunchCentre.makeCustomToastText("successfully generated boards for debug",getBaseContext());
                CustomScrollView customScrollView = findViewById(R.id.scrollable_loadablegames);
                customScrollView.setLoadingList(boardList);
                customScrollView.setNestedScrollingEnabled(true);
                customScrollView.setAdapter(new LoaderAdapter((ArrayList<Board>) boardList, customScrollView.getContext()));
            }
        });
    }

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
