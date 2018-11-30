package fall2018.csc2017.slidingtiles.UltimateTTT;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class UltimateTTTInfoManager {
    //Adapted from: https://github.com/Prakash2403/UltimateTicTacToe/blob/master/app/src/main/java/com/example/prakash/ultimatetictactoe/json/jsonparser.java

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

    static JSONObject getMapToJson(Map<String, String> mapData) {
//        Adapted from toJson in Gamestates
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CurrentActiveBlock", mapData.get("CurrentActiveBlock"));
            jsonObject.put("CurrentCell", mapData.get("CurrentCell"));
            jsonObject.put("NextActiveBlock", mapData.get("NextActiveBlock"));
            jsonObject.put("CurrentWinner", mapData.get("CurrentWinner"));
            jsonObject.put("GlobalWinner", mapData.get("GlobalWinner"));
            jsonObject.put("Turn", mapData.get("Turn"));
            jsonObject.put("DisableBlock", mapData.get("DisableBlock"));
            jsonObject.put("DisableList", mapData.get("DisableList"));
            jsonObject.put("ResetList", mapData.get("ResetList"));
            jsonObject.put("ScoreP1", mapData.get("ScoreP1"));
            jsonObject.put("ScoreP2", mapData.get("ScoreP2"));
            jsonObject.put("ResetBlockColor", mapData.get("ResetBlockColor"));
            jsonObject.put("ButtonPressed", mapData.get("ButtonPressed"));
        } catch (JSONException jsonException) {
            System.out.println("Exception in converting to JSON");
        }
        return jsonObject;
    }
}
