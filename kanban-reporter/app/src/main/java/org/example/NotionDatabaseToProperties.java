package org.example;

import java.util.HashMap;

import com.google.gson.JsonObject;

public class NotionDatabaseToProperties {
    private final String ID_DIR = "id";

    public HashMap<String, String> getProperty(JsonObject data, String propertyName) {
        String dir;
        HashMap<String, String> result = new HashMap<String, String>();
        switch (propertyName) {
            case "id":
                dir = ID_DIR;
                break;
        
            default:
                return null;
        }
        result.put(propertyName, getPropertyByFullDir(data, dir));
        return result;
    }

    private String getPropertyByFullDir(JsonObject data, String dir) {
        JsonObject curData = data.deepCopy();
        String[] dirs = dir.split("/");

        for (int i=0; i<dirs.length-1; i++) {
            curData = getPropertyByNextDir(data, dirs[i]);
        }
        
        try {
            return curData.get(dirs[dirs.length-1]).getAsString();
        } catch (Exception e) {
            return "";
        }
    }

    private JsonObject getPropertyByNextDir(JsonObject data, String nextDir) {
        return data.get(nextDir).getAsJsonObject();
    }
}
