package org.example;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class NotionToSlack {
    private final String ENVIROMENT;

    public NotionToSlack(String environment) {
        ENVIROMENT = environment;
    }

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
                        assigneesString += slackIdToSlackTag(slackId);
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

    private String notionIdToSlackId(String notionId) {
        switch (notionId) {
            case "b5d64ee2-1da8-4eb5-a359-e96da5c99a32": // LHD
                return "U06BEHTT2M8";
            case "84254454-3dc7-44e1-808f-7fe2b363a217": // SDY
                return "U04EC1QEP6V";
            case "851916c0-e459-4cb7-93e9-6385c0875ca5": // LSM
                return "U06B01WCLKH";
            case "43786bb7-9fd1-4afa-82a4-e0dc89cd2b3f": // SWJ
                return "U04EC1TMR0R";
            case "542fe32c-491e-40f9-ab67-070ed261a5ed": // WHJ
                return "U04E1RX2Y20";
            case "4b9b7d3c-59e9-41f6-9beb-6f11bff8352b": // NKT
                return "U06B01WQQK1";
            case "ad0c202e-ca11-456d-91d2-a1d44b13a8a8": // JJA
                return "U06BT6ZU741";
            case "e3c4232e-41ce-4189-90f4-121c7cda69f8": // CYJ
                return "U04F0NCC9L4";
            case "6812fadd-2cbd-42f2-bc2c-0fc11eb7efed": // JYJ
                return "U05RMKRK1HR";
            default:
                return null;
        }
    }

    private String dueStateTodueString(DueState dueState) {
        return dueState.toString();
    }

    private String slackIdToSlackTag(String slackId) {
        if (ENVIROMENT == "prod") return ("<@" + slackId + "> ");
        else if (ENVIROMENT == "dev") return ("@" + slackId + " ");
        else return "";
    }

    private enum DueState {
        LEFT_ENOUTH, ONE_DAY_LEFT, THE_DAY, EXCEEDED_A_LITTLE, EXCEEDED_TOO_MUCH
    }
}
