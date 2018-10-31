package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
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
/**
 * The launch centre, which is a login screen
 */
public class LaunchCentre extends AppCompatActivity {
    /**
    * Account data storage file
     */
    public static final String ACCOUNTS_FILENAME = "account_file.ser";
    /**
     * Account list, to be loaded from local file
     */
    private List<Account> accountsList = new ArrayList<>();
    /**
     * Front-end texts interfaces for Username/Password Input
     */
    private EditText userTextField, passwordTextField;
    /**
     * Current user's name
     */
    private static String currentUser;
    private SharedPreferences sharedPreferences;
    private CheckBox rememberCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchcentre);
        loadAccountsFromFile(ACCOUNTS_FILENAME);
        addPasswordOnKeyListener();
        userTextField = findViewById(R.id.text_username);
        passwordTextField = findViewById(R.id.text_password);
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        rememberCheckbox = findViewById(R.id.cb_remember);
        retrievePrefs();
    }
    /**
     * Saves current list of accounts to fileName
     * @param fileName the name of the file
     */
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
    /**
     * Load list of accounts to accountsList
     * @param fileName the name of the file
     */
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
    /**
     * Password field can directly login using the Enter button
     */
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
    /**
     * On click function for the register button
     * @param v the current view(Called by application)
     */
    public void registerButtonOnClick(View v){
        String regUsername = userTextField.getText().toString();
        String regPassword = passwordTextField.getText().toString();
        Account newAccount = new Account(regUsername, regPassword);
        if(regUsername.equals("Guest") || regUsername.equals("guest")){
            makeCustomToastText("Account name reserved for guests only!", getBaseContext());
        }
        else if(!checkExistingAccount(newAccount)){
            accountsList.add(newAccount);
            saveCredentialsToFile(ACCOUNTS_FILENAME);
            makeCustomToastText("Account creation successful!", getBaseContext());
        }
        else{
            makeCustomToastText("Account already exists!", getBaseContext());
        }
    }
    /**
     * On click function for the login button
     * @param v the current view(Called by application)
     */
    public void loginButtonOnClick(View v){
        if(authUser()) {
            makeCustomToastText("Login successful!", getBaseContext());
            if(rememberCheckbox.isChecked())
                rememberPrefStore();
            Intent tmp = new Intent(v.getContext(), GameSelection.class);
            tmp.putExtra("currentUser", currentUser);
            startActivity(tmp);
        }
        else{
            makeCustomToastText("Wrong credentials, please try again!", getBaseContext());
        }
    }
    /**
     * Stores username, password, and "remember me" checkbox's state using prefs
     */
    private void rememberPrefStore(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("remember", true);
        editor.putString("previousUser", currentUser);
        editor.putString("previousPass", passwordTextField.getText().toString());
        editor.apply();
    }
    /**
     * Retrieves username, password, and "remember me" checkbox's state using prefs
     */
    private void retrievePrefs(){
        rememberCheckbox.setChecked(sharedPreferences.getBoolean("remember", false));
        userTextField.setText(sharedPreferences.getString("previousUser", ""));
        passwordTextField.setText(sharedPreferences.getString("previousPass", ""));
    }
    /**
     * On click function for the guest button
     * @param v the current view(Called by application)
     */
    public void guestButtonOnClick(View v){
        Intent tmp = new Intent(v.getContext(), GameSelection.class);
        currentUser = "-1";
        tmp.putExtra("currentUser", currentUser);
        startActivity(tmp);
    }
    /**
     * Authenticates the user with it's corresponding login details input
     * @return whether if input credentials match
     */
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
    /**
     * Checks if account already exists
     * @param account the account to be checked
     * @return whether if account already exists
     */
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
    /**
     * Static function for creating custom Toast messages
     * @param displayText, the string to be displayed
     * @param ctx the current context
     */
    public static void makeCustomToastText(String displayText, Context ctx)
    {
        Toast.makeText(ctx, displayText, Toast.LENGTH_SHORT).show();
    }
}
