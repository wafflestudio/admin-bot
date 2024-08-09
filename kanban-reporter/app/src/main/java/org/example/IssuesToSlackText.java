package org.example;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.example.records.Issue;

public class IssuesToSlackText {
    public static ArrayList<String> issuesToTexts(String environment, ArrayList<Issue> issues) {
        ArrayList<String> result = new ArrayList<String>();
        for (Issue issue : issues) {
            result.add(issueToText(environment, issue));
        }
        return result;
    }

    private static String issueToText(String environment, Issue issue) {
        Boolean noDue = checkNoDue(issue);
        Boolean noAssignees = checkNoAssignees(issue);
        Boolean noTitle = checkNoTitle(issue);
        Boolean propertyAbsence = (noDue || noAssignees || noTitle);

        DueState dueState = DueState.LEFT_ENOUTH;
        String due = null;
        if (!noDue) {
            if (issue.end() == null) {
                due = issue.getStart().substring(0, 10);
            }
            else {
                due = issue.getEnd().substring(0, 10);
            }

            dueState = decideDueState(due);
        }

        Boolean toMakeComment = (propertyAbsence || (dueState != DueState.LEFT_ENOUTH));
        if (toMakeComment) {
            // fix: 각 라인들에 대해 함수 분리를 할 수 있다면..?

            // Line 1
            String assigneesString = "";
            for (String notionId : issue.getAssignees()) {
                String slackId = notionIdToSlackId(environment, notionId);
                if (slackId == null) continue;
                else {
                    assigneesString += slackIdToSlackTag(environment, slackId);
                }
            }

            String issueLink = GetResources.getProperty("KANBAN_BASE_URL") + issue.getId().replace("-", "");
            String lineOne = String.format("%s <%s|%s>", assigneesString, issueLink, issue.getTitle());

            // Line 2
            String lineTwo = "";
            if (propertyAbsence) {
                lineTwo += "\n";
                if (noTitle) lineTwo += GetResources.getProperty("ABSENCE_TITLE");
                if (noAssignees) lineTwo += GetResources.getProperty("ABSENCE_ASSIGNEE");
                if (noDue) lineTwo += GetResources.getProperty("ABSENCE_DUE");;
                lineTwo = lineTwo.replaceAll(", $", "");
                lineTwo += GetResources.getProperty("ABSENCE_SOMETHING");;
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

    private static Boolean checkNoDue(Issue issue) {
        return (issue.start() == null);
    }

    private static Boolean checkNoAssignees(Issue issue) {
        ArrayList<String> assignees = issue.assignees();
        return (assignees == null || assignees.isEmpty());
    }

    private static Boolean checkNoTitle(Issue issue) {
        return (issue.title() == null);
    }

    private static DueState decideDueState(String due) {
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

    private static String notionIdToSlackId(String environment, String notionId) {
        if (notionId.equals(GetResources.getProperty("NOTION_ID_LHD", environment))){
            return GetResources.getProperty("SLACK_ID_LHD", environment);
        }
        else if (notionId.equals(GetResources.getProperty("NOTION_ID_SDY", environment))){
            return GetResources.getProperty("SLACK_ID_SDY", environment);
        }
        else if (notionId.equals(GetResources.getProperty("NOTION_ID_LSM", environment))){
            return GetResources.getProperty("SLACK_ID_LSM", environment);
        }
        else if (notionId.equals(GetResources.getProperty("NOTION_ID_SWJ", environment))){
            return GetResources.getProperty("SLACK_ID_SWJ", environment);
        }
        else if (notionId.equals(GetResources.getProperty("NOTION_ID_WHJ", environment))){
            return GetResources.getProperty("SLACK_ID_WHJ", environment);
        }
        else if (notionId.equals(GetResources.getProperty("NOTION_ID_NKT", environment))){
            return GetResources.getProperty("SLACK_ID_NKT", environment);
        }
        else if (notionId.equals(GetResources.getProperty("NOTION_ID_JJA", environment))){
            return GetResources.getProperty("SLACK_ID_JJA", environment);
        }
        else if (notionId.equals(GetResources.getProperty("NOTION_ID_CYJ", environment))){
            return GetResources.getProperty("SLACK_ID_CYJ", environment);
        }
        else if (notionId.equals(GetResources.getProperty("NOTION_ID_JYJ", environment))){
            return GetResources.getProperty("SLACK_ID_JYJ", environment);
        }
        else return null;
    }

    private static String dueStateTodueString(DueState dueState) {
        return GetResources.getProperty(dueState.toString());
    }

    private static String slackIdToSlackTag(String environment, String slackId) {
        if (environment == "prod") return ("<@" + slackId + "> ");
        else if (environment == "dev") return ("@" + slackId + " ");
        else return "";
    }

    private enum DueState {
        LEFT_ENOUTH, ONE_DAY_LEFT, THE_DAY, EXCEEDED_A_LITTLE, EXCEEDED_TOO_MUCH
    }
}
