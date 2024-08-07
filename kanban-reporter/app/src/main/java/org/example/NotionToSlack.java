package org.example;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class NotionToSlack {
    public ArrayList<String> issuesToTexts(ArrayList<HashMap<String, String>> issues) {
        ArrayList<String> result = new ArrayList<String>();
        for (HashMap<String,String> issue : issues) {
            result.add(issueToText(issue));
        }
        return result;
    }

    private String issueToText(HashMap<String,String> issue) {
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
            // fix: 각 라인들에 대해 함수 분리를 할 수 있다면..?

            // Line 1
            String assigneesString = "";
            Integer assigneeNumber = 0;
            while (true) {
                assigneeNumber++;
                String notionId = issue.getOrDefault("assignee" + assigneeNumber, null);
                if (notionId == null) break;
                else {
                    String slackId = notionIdToSlackId(notionId);
                    if (slackId == null) continue;
                    else {
                        assigneesString += ("<@" + slackId + "> ");
                    }
                }
            }
            String issueLink = "https://www.notion.so/" + issue.getOrDefault("id", "").replace("-", "");
            String lineOne = String.format("%s <%s|%s>", assigneesString, issueLink, issue.getOrDefault("title", ""));

            // Line 2
            String lineTwo = "";
            if (propertyAbsence) {
                lineTwo += "\n";
                if (noTitle) lineTwo += "제목, ";
                if (noAssignees) lineTwo += "배정, ";
                if (noDue) lineTwo += "기한, ";
                lineTwo = lineTwo.replaceAll(", $", "");
                lineTwo += "이 없습니다. 해당 속성을 채워주세요!";
            }

            // Line 3
            String lineThree = "";
            if (dueState != DueState.LEFT_ENOUTH) {
                lineThree += "\n";
                lineThree += dueStateTodueString(dueState);
            }

            return lineOne + lineTwo + lineThree;
        }
        else return "";
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

    private String notionIdToSlackId(String notionId) {
        switch (notionId) {
            case "b5d64ee2-1da8-4eb5-a359-e96da5c99a32":
                return "plgafhd";
        
            default:
                return null;
        }
    }

    private String dueStateTodueString(DueState dueState) {
        return dueState.toString();
    }
}
