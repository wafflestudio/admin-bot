package org.example;

import java.util.ArrayList;
import java.util.HashMap;

public class NotionToSlack {
    public void issuesToTexts(ArrayList<HashMap<String, String>> issues) {
        for (HashMap<String,String> issue : issues) {
            issueToText(issue);
        }
    }

    private void issueToText(HashMap<String,String> issue) {
        Boolean noDue = checkNoDue(issue);
        Boolean noAssignees = checkNoAssignees(issue);
        Boolean noTitle = checkNoTitle(issue);
        Boolean propertyAbsence = (noDue || noAssignees || noTitle);
    }

    private Boolean checkNoDue(HashMap<String,String> issue) {
        String start = issue.getOrDefault("start",  null);
        String end = issue.getOrDefault("end",  null);
        return (start == null && end == null);
    }

    private Boolean checkNoAssignees(HashMap<String,String> issue) {
        String firstAssignees = issue.getOrDefault("assignee1",  null);
        return (firstAssignees == null);
    }

    private Boolean checkNoTitle(HashMap<String,String> issue) {
        String title = issue.getOrDefault("title",  null);
        return (title == null);
    }
}
