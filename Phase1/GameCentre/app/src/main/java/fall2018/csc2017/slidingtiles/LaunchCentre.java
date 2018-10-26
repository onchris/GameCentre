package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class LaunchCentre extends AppCompatActivity {

    public static final String ACCOUNTS_FILENAME = "account_file.ser";
    private List<Account> accountsList = new ArrayList<>();
    private EditText userTextField;
    private EditText passwordTextField;
    private static String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchcentre);
        loadAccountsFromFile(ACCOUNTS_FILENAME);
        addGuestButtonListener();
        addRegisterButtonListener();
        addLoginButtonListener();
        addPasswordOnKeyListener();
        userTextField = findViewById(R.id.text_username);
        passwordTextField = findViewById(R.id.text_password);
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

    private void addPasswordOnKeyListener(){
        EditText passwordField = findViewById(R.id.text_password);
        passwordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    Button loginButton = findViewById(R.id.button_login);
                    loginButton.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    private void addRegisterButtonListener(){
        Button registerButton = findViewById(R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regUsername = userTextField.getText().toString();
                String regPassword = passwordTextField.getText().toString();
                Account newAccount = new Account(regUsername, regPassword);
                if(regUsername.equals("Guest") || regUsername.equals("guest")){
                    makeCustomToastText("Account name reserved for guests only!");
                }
                else if(!checkExistingAccount(newAccount)){
                    accountsList.add(newAccount);
                    saveCredentialsToFile(ACCOUNTS_FILENAME);
                    makeCustomToastText("Account creation successful!");
                }
                else{
                    makeCustomToastText("Account already exists!");
                }
            }
        });
    }
    private boolean checkExistingAccount(Account account){
        if(accountsList.isEmpty())
            return false;
        for(Account existingAccount: accountsList)
        {
            if(existingAccount.equals(account))
                return true;
        }
        return false;
    }

    private void addLoginButtonListener() {
        Button loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(authUser()) {
                    makeCustomToastText("Login sucessful!");
                    Intent tmp = new Intent(v.getContext(), GameSelection.class);
                    tmp.putExtra("currentUser", currentUser);
                    startActivity(tmp);
                }
                else{
                    makeCustomToastText("Wrong credentials, please try again!");
                }
            }
        });
    }

    private void makeCustomToastText(String displayText)
    {
        Toast.makeText(this, displayText, Toast.LENGTH_SHORT).show();
    }

    private void addGuestButtonListener() {
        Button guestButton = findViewById(R.id.button_guest);
        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tmp = new Intent(v.getContext(), GameSelection.class);
                tmp.putExtra("currentUser", currentUser);
                currentUser = "-1";
                startActivity(tmp);
            }
        });
    }


    public boolean authUser(){
        String username = userTextField.getText().toString();
        String password = passwordTextField.getText().toString();
        for(Account acc: accountsList)
        {
            if(username.equals(acc.getUsername())&& password.equals(acc.getPassword())){
                currentUser = username;
                return true;
            }
        }
        return false;
    }
}
