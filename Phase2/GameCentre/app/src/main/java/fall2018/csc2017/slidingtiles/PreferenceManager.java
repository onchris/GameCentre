package fall2018.csc2017.slidingtiles;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

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

    public void storeString(String key, String value){
        editor.putString(key, value);
        editor.apply();
    }
    public String retrieveString(String key, String defaultNotFound){
        return preferences.getString(key, defaultNotFound);
    }

    public void storeBool(String key, Boolean value){
        editor.putBoolean(key, value);
        editor.apply();
    }
    public boolean retrieveBool(String key, boolean defaultNotFound){
        return preferences.getBoolean(key, defaultNotFound);
    }

    public void storeLoginData(String username, String password, Boolean rememberMe){
        storeBool("remember", true);
        storeString("previousUser", username);
        storeString("previousPass", password);
    }
    public void wipeLoginData(){
        editor.remove("remember");
        editor.remove("previousUser");
        editor.remove("previousPass");
        editor.apply();
    }
}
