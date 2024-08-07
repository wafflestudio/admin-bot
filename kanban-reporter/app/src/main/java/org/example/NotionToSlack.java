package org.example;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

        DueState dueState = DueState.LEFT_ENOUTH;
        String due = null;
        if (!noDue) {
            if (issue.getOrDefault("end", null) == null) {
                due = issue.getOrDefault("start", "2099-12-31").substring(0, 10);
            }
            else {
                due = issue.getOrDefault("end", "2099-12-31").substring(0, 10);
            }

            dueState = decideDueState(due);
        }

        Boolean toMakeComment = (propertyAbsence || (dueState != DueState.LEFT_ENOUTH));
        if (toMakeComment) {
            System.out.println(issue.get("title") + " " + dueState);
        }
    }

    private Boolean checkNoDue(HashMap<String,String> issue) {
        String start = issue.getOrDefault("start",  null);
        return (start == null);
    }

    private Boolean checkNoAssignees(HashMap<String,String> issue) {
        String firstAssignees = issue.getOrDefault("assignee1",  null);
        return (firstAssignees == null);
    }

    private Boolean checkNoTitle(HashMap<String,String> issue) {
        String title = issue.getOrDefault("title",  null);
        return (title == null);
    }

    private DueState decideDueState(String due) {
        try {
            LocalDate dueDate = LocalDate.parse(due);
            LocalDate nowDate = LocalDate.now();
            Long daysExceeded = ChronoUnit.DAYS.between(dueDate, nowDate);

            if (daysExceeded < -1) return DueState.LEFT_ENOUTH;
            else if (daysExceeded < 0) return DueState.ONE_DAY_LEFT;
            else if (daysExceeded < 1) return DueState.THE_DAY;
            else if (daysExceeded < 4) return DueState.EXCEEDED_A_LITTLE;
            else return DueState.EXCEEDED_TOO_MUCH;
        } catch (Exception e) {
            return DueState.LEFT_ENOUTH;
        }
    }

    private enum DueState {
        LEFT_ENOUTH, ONE_DAY_LEFT, THE_DAY, EXCEEDED_A_LITTLE, EXCEEDED_TOO_MUCH
    }
}
