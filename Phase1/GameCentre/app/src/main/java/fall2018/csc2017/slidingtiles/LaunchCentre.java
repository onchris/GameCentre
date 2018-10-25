package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class LaunchCentre extends AppCompatActivity {

    public static final String SAVE_FILENAME = "account_file.ser";
    private List<Account> accountsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchcentre);
        addGuestButtonListener();
    }

    public void saveCredentialsToFile(String fileName){
        try{
            ObjectOutputStream outputStream =
                    new ObjectOutputStream(this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(accountsList);
            outputStream.close();
        } catch (IOException e){
            Log.e("Exception", "Credential write failed: " + e.toString());
        }
    }
    public void loadAccountsFromFile(String fileName){
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                accountsList = (ArrayList<Account>) input.readObject();
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

    private void addLoginButtonListener() {
        Button loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(authUser()) {
                    Intent tmp = new Intent(v.getContext(), GameActivity.class);
                }
            }
        });
    }

    private void addGuestButtonListener() {
        Button guestButton = findViewById(R.id.button_guest);
        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tmp = new Intent(v.getContext(), GameSelection.class);
                startActivity(tmp);
            }
        });
    }


    public boolean authUser(){
        EditText userTextField = findViewById(R.id.text_username);
        EditText passwordTextField = findViewById(R.id.text_password);
        String username = userTextField.getText().toString();
        String password = passwordTextField.getText().toString();
        for(Account acc: accountsList)
        {
            if(username.equals(acc.getUsername())&& password.equals(acc.getPassword())){
                return true;
            }
        }
        return false;
    }
}
