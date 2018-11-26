package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static fall2018.csc2017.slidingtiles.UtilityManager.ACCOUNTS_FILENAME;
import static fall2018.csc2017.slidingtiles.UtilityManager.loadAccountList;
import static fall2018.csc2017.slidingtiles.UtilityManager.makeCustomToastText;

/**
 * The launch centre, which is a login screen
 */
public class LaunchCentre extends AppCompatActivity {
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
    /**
     * The checkbox of which user can select to remember credentials upon restart
     */
    private CheckBox rememberCheckbox;
    /**
     * The PreferenceManager that handles preferences retrieving and storing
     */
    private PreferenceManager preferenceManager;
    /**
     * The AccountManager that handles account related methods
     */
    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchcentre);
        accountsList = loadAccountList(this);
        accountManager = new AccountManager(accountsList);
        preferenceManager = new PreferenceManager(this);
        addPasswordOnKeyListener();
        userTextField = findViewById(R.id.text_username);
        passwordTextField = findViewById(R.id.text_password);
        rememberCheckbox = findViewById(R.id.cb_remember);
        preferenceManager.setWidgetPreferences();
    }
    /**
     * Gets the account manager.
     * @return account manager
     */
    public AccountManager getAccountManager() {
        return accountManager;
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
        Account newAccount = accountManager.createNewAccount(regUsername, regPassword, this);
        if(newAccount != null){
            accountManager.saveCredentials(ACCOUNTS_FILENAME, this);
        }
    }
    /**
     * On click function for the login button
     * @param v the current view(Called by application)
     */
    public void loginButtonOnClick(View v){
        String username = userTextField.getText().toString();
        String password = passwordTextField.getText().toString();
        if(accountManager.authenticateCredentials(username, password)) {
            currentUser = username;
            makeCustomToastText(this.getString(R.string.lc_login_successful), getBaseContext());
            if(rememberCheckbox.isChecked())
                preferenceManager.storeLoginData(currentUser,
                        passwordTextField.getText().toString(),
                        true);
            else
                preferenceManager.wipeLoginData();
            Intent tmp = new Intent(v.getContext(), GameSelection.class);
            tmp.putExtra("currentUser", currentUser);
            startActivity(tmp);
        }
        else{
            makeCustomToastText(this.getString(R.string.lc_wrong_credentials), getBaseContext());
        }
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
}