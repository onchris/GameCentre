package fall2018.csc2017.slidingtiles.UltimateTTT;

/*
Adapted from: https://github.com/Prakash2403/UltimateTicTacToe/blob/master/app/src/main/java/com/example/prakash/ultimatetictactoe/json/jsonparser.java
https://github.com/Prakash2403/UltimateTicTacToe/blob/master/app/src/main/java/com/example/prakash/ultimatetictactoe/backend/Backend.java
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for Ultimate Tic Tac Toe info manager.
 */
class UltimateTTTInfoManager {
    /**
     * Stores data in map
     *
     * @param jsonData data to be stored in map
     * @return the map with all data entered
     */
    static Map<String, String> parseJson(JSONObject jsonData) {
        HashMap<String, String> map;
        JSONObject jsonObject;
        jsonObject = jsonData;
        map = new HashMap<String, String>();
        try {
            map.put("CurrentWinner", jsonObject.getString("CurrentWinner"));
            map.put("GlobalWinner", jsonObject.getString("GlobalWinner"));

            map.put("CurrentActiveBlock", jsonObject.getString("CurrentActiveBlock"));
            map.put("NextActiveBlock", jsonObject.getString("NextActiveBlock"));
            map.put("DisableBlock", jsonObject.getString("DisableBlock"));

            map.put("ScoreP1", jsonObject.getString("ScoreP1"));
            map.put("ScoreP2", jsonObject.getString("ScoreP2"));

            map.put("CurrentCell", jsonObject.getString("CurrentCell"));
            map.put("DisableList", jsonObject.getString("DisableList"));
            map.put("ResetList", jsonObject.getString("ResetList"));

            map.put("ButtonPressed", jsonObject.getString("ButtonPressed"));
            map.put("Turn", jsonObject.getString("Turn"));
            map.put("ResetBlockColor", jsonObject.getString("ResetBlockColor"));
        } catch (JSONException jsonException) {
            System.err.println(jsonException);
        }
        return map;
    }
}
