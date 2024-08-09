package org.example;

import com.slack.api.Slack;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

public class SlackBot {
    public static String createThread(String slackBotToken, String slackChannelId, String title) {
        Slack slack = Slack.getInstance();

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
            .token(slackBotToken)
            .channel(slackChannelId)
            .text(title)
            .build();

        try {
            ChatPostMessageResponse response = slack.methods().chatPostMessage(request);

            if (response.isOk()) {
                return response.getTs();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean createComment(String slackBotToken, String slackChannelId, String parentTs, String content) {
        Slack slack = Slack.getInstance();

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
            .token(slackBotToken)
            .channel(slackChannelId)
            .text(content)
            .threadTs(parentTs)
            .build();

        try {
            ChatPostMessageResponse response = slack.methods().chatPostMessage(request);

            if (response.isOk()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
