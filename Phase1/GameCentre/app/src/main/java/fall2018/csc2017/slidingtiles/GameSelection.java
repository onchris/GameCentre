package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class GameSelection extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        addLoginButtonListener();
    }

    private void addLoginButtonListener() {
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
