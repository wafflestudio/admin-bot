package org.example;

import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class NotionDatabaseToProperties {
    private final String ID_DIR = "id";
    private final String TITLE_DIR = "properties/이름/title-0/text/content";
    private final String START_DIR = "properties/기한/date/start";
    private final String END_DIR = "properties/기한/date/end";

    public HashMap<String, String> getProperty(JsonObject data, String propertyName) {
        String dir;
        HashMap<String, String> result = new HashMap<String, String>();
        switch (propertyName) {
            case "id":
                dir = ID_DIR;
                break;

            case "title":
                dir = TITLE_DIR;
                break;

            case "start":
                dir = START_DIR;
                break;

            case "end":
                dir = END_DIR;
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
            curData = getPropertyByNextDir(curData, dirs[i]);
        }
        
        try {
            return curData.get(dirs[dirs.length-1]).getAsString();
        } catch (Exception e) {
            return null;
        }
    }

    private JsonObject getPropertyByNextDir(JsonObject data, String nextDir) {
        String[] dirs = nextDir.split("-");

        // fix: 코드가 살짝 좋지 않아 보인다..
        // fix: 숫자가 들어간 디렉토리가 맨 마지막이면 문제가 발생할 것 같다.
        // fix: parse에 대한 예외 처리
        if (dirs.length == 0) return new JsonObject();
        else if (dirs.length == 1) return data.get(nextDir).getAsJsonObject();
        else {
            JsonArray curDataArray = data.get(dirs[0]).getAsJsonArray();
            for (int i=1; i<dirs.length-1; i++) {
                curDataArray = curDataArray.get(Integer.parseInt(dirs[i])).getAsJsonArray();
            }
            return curDataArray.get(Integer.parseInt(dirs[dirs.length-1])).getAsJsonObject();
        }
    }
}