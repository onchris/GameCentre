package fall2018.csc2017.slidingtiles;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    /**
     * SharedPreferences editor that handles storing preferences
     */
    private SharedPreferences.Editor editor;
    /**
     * SharedPreferences that handles retrieving preferences
     */
    private SharedPreferences preferences;

    /**
     * PreferenceManager Constructor, instantiates preferences and it's editor
     * @param currentActivity the currentActivity, accessing method getPreferences.
     */
    public PreferenceManager(Activity currentActivity){
        preferences = currentActivity.getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    /**
     * Stores a String by a retrievable key which associates with the specified string
     * @param key the key where it's value will assign to.
     * @param value the value to be assigned to the key and stored
     */
    public void storeString(String key, String value){
        editor.putString(key, value);
        editor.apply();
    }
    /**
     * Retrieves a String based on the key, if not found, defaultNotFound would be returned
     * @param key the key where it's value will be retrieved from
     * @param defaultNotFound the default value when key is not found in SharedPreferences
     * @return the String that is associated with the specified key, or defaultNotFound if None.
     */
    public String retrieveString(String key, String defaultNotFound){
        return preferences.getString(key, defaultNotFound);
    }
    /**
     * Storing a boolean by a retrievable key which associates with the specified boolean
     * @param key the key where it's value will assign to.
     * @param value the value to be assigned to the key and stored
     */
    public void storeBool(String key, boolean value){
        editor.putBoolean(key, value);
        editor.apply();
    }
    /**
     * Retrieves a boolean based on the key, if not found, defaultNotFound would be returned
     * @param key the key where it's value will be retrieved from
     * @param defaultNotFound the default value when key is not found in SharedPreferences
     * @return the boolean that is associated with the specified key, or defaultNotFound if None.
     */
    public boolean retrieveBool(String key, boolean defaultNotFound){
        return preferences.getBoolean(key, defaultNotFound);
    }
    /**
     * Stores login data associated with the login screen
     * @param username the username of the login screen.
     * @param password the password of the login screen.
     * @param rememberMe the checkbox of the login screen, checked means true
     */
    public void storeLoginData(String username, String password, boolean rememberMe){
        storeBool("remember", true);
        storeString("previousUser", username);
        storeString("previousPass", password);
    }
    /**
     * Removes login data associated with the login screen.
     */
    public void wipeLoginData(){
        editor.remove("remember");
        editor.remove("previousUser");
        editor.remove("previousPass");
        editor.apply();
    }
}
