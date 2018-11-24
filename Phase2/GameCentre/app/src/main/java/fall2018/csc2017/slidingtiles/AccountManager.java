package fall2018.csc2017.slidingtiles;

import android.app.Activity;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static fall2018.csc2017.slidingtiles.UtilityManager.makeCustomToastText;

public class AccountManager {
    private final List<Account> accountsList;
    private Account currentAccount;
    public AccountManager(List<Account> accountsList){
        this.accountsList = accountsList;
    }
    /**
     * Checks if account already exists
     * @param username the account to be checked
     * @return whether if account already exists
     */
    public boolean checkExistingUser(String username){
        if(accountsList.isEmpty())
            return false;
        for(Account existingAccount: accountsList)
        {
            if(username.equals(existingAccount.getUsername()))
                return true;
        }
        return false;
    }
    /**
     * Authenticates the user with it's corresponding login details input
     * @param username the username to be checked
     * @return whether if input credentials match
     */
    public boolean authenticateCredentials(String username, String password){
        for(Account acc: accountsList)
        {
            if(username.equals(acc.getUsername())&& password.equals(acc.getPassword())){
                return true;
            }
        }
        return false;
    }

    public Account getAccountFromUsername(String name){
        for(Account acc: accountsList)
        {
            if(name.equals(acc.getUsername())){
                return acc;
            }
        }
        return null;
    }
    /**
     * Gets the current accounts' board list
     */
    public List<BoardManager> getCurrentAccountBoardList(Account account, boolean guest){
        List<BoardManager> bm;
        bm = guest ? new ArrayList<BoardManager>() :
                account != null ? account.getBoardList() : new ArrayList<BoardManager>();
        return bm;
    }

    /**
     * Creates a new account based on requirements
     * @param username the username that the account to be created with
     * @param password the password that associates to the account
     * @param activity the activity this manager is in for toast messages
     * @return A new account, null if invalid credentials or existing account
     */
    public Account createNewAccount(String username, String password, Activity activity){
        Account account;
        if(username.equals("Guest") || username.equals("guest")){
            makeCustomToastText("Account name reserved for guests only!", activity);
        } else if (username.equals("") || username.equals("")){
            makeCustomToastText("Fields cannot be empty!", activity);
        } else if (username.length() < 3 || password.length() < 3 ) {
            makeCustomToastText("Fields cannot have less than 3 characters!", activity);
        } else if (!checkExistingUser(username)){
            account = new Account(username, password);
            accountsList.add(account);
            return account;
        } else if (checkExistingUser(username))
        {
            makeCustomToastText("User already exists!", activity);
        }
        return null;
    }

    /**
     * Saves current list of accounts to fileName
     * @param fileName the name of the file
     * @param currentActivity the activity this manager is in for openFileOutput() accessing.
     */
    public void saveCredentials(String fileName, Activity currentActivity){
        try{
            ObjectOutputStream outputStream =
                    new ObjectOutputStream(currentActivity.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(accountsList);
            outputStream.close();
            makeCustomToastText("Account credentials saved!", currentActivity);
        } catch (IOException e){
            makeCustomToastText("Unable to save credentials in AccountManager!", currentActivity);
        }
    }
}
