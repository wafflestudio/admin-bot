package org.example.records;

import java.util.ArrayList;

public record Issue(
    String id,
    String title,
    String start,
    String end,
    ArrayList<String> assignees
) {
    public String getId() {
        if (id == null) return "";
        else return id;
    }

    public String getTitle() {
        if (title == null) return "";
        else return title;
    }

    public String getStart(){
        if (start == null) return "2099-12-31";
        else return start;
    }

    public String getEnd(){
        if (end == null) return "2099-12-31";
        else return end;
    }

    public ArrayList<String> getAssignees(){
        if (assignees == null) return new ArrayList<String>();
        else return assignees;
    }
}
