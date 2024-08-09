package org.example;

import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class KanbanJsonToProperty {
    public HashMap<String, String> getProperty(JsonObject data, String propertyName) {
        String dir;
        HashMap<String, String> result = new HashMap<String, String>();
        switch (propertyName) {
            case "id":
                dir = GetResources.getProperty("KANBAN_ID_DIR");
                break;

            case "title":
                dir = GetResources.getProperty("KANBAN_TITLE_DIR");
                break;

            case "start":
                dir = GetResources.getProperty("KANBAN_START_DIR");
                break;

            case "end":
                dir = GetResources.getProperty("KANBAN_END_DIR");
                break;

            case "assignees":
                dir = GetResources.getProperty("KANBAN_ASSIGNEES_DIR");
                return getAssignees(data, dir);
        
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
        // fix: 예외 처리가 깔끔하지 않다.

        try {
            if (dirs.length == 0) return new JsonObject();
            else if (dirs.length == 1) return data.get(nextDir).getAsJsonObject();
            else {
                JsonArray curDataArray = data.get(dirs[0]).getAsJsonArray();
                for (int i=1; i<dirs.length-1; i++) {
                    curDataArray = curDataArray.get(Integer.parseInt(dirs[i])).getAsJsonArray();
                }
                return curDataArray.get(Integer.parseInt(dirs[dirs.length-1])).getAsJsonObject();
            }
        } catch (Exception e) {
            return null;
        }
    }

    // fix: 당장 구현을 위해 따로 분리했는데, 일반화를 할 수만 있다면..
    private HashMap<String, String> getAssignees(JsonObject data, String dir) {
        JsonObject curData = data.deepCopy();
        HashMap<String, String> result = new HashMap<String, String>();

        String[] dirs = dir.split("/");
        for (int i=0; i<dirs.length-1; i++) {
            curData = getPropertyByNextDir(curData, dirs[i]);
        }

        JsonArray assigneesJsonArray = curData.getAsJsonArray(dirs[dirs.length-1]);
        for (int i=0; i<assigneesJsonArray.size(); i++) {
            result.put("assignee"+(i+1), assigneesJsonArray.get(i).getAsJsonObject().get("id").getAsString());
        }

        return result;
    }
}