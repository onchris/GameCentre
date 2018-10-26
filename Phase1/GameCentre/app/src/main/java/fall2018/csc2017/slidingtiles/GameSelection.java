package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameSelection extends AppCompatActivity {
    private TextView currentUserTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        currentUserTextView = findViewById(R.id.text_loggedas);
        if(!getIntent().getStringExtra("currentUser").equals("-1"))
            currentUserTextView.setText(getIntent().getStringExtra("currentUser"));
        else
            currentUserTextView.setText("Guest");
        addSlidingGameButtonListener();
    }

    private void addSlidingGameButtonListener() {
        ImageButton game1Button = findViewById(R.id.button_gameselect1);
        game1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tmp = new Intent(v.getContext(), StartingActivity.class);
                startActivity(tmp);

            }
        });
    }


}
