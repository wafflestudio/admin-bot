package org.example;

import com.slack.api.Slack;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

public class SlackBot {
    private final String SLACK_BOT_TOKEN;
    private final String SLACK_CHANNEL_ID;

    public SlackBot(String token, String channelId) {
        SLACK_BOT_TOKEN = token;
        SLACK_CHANNEL_ID = channelId;
    }

    public String createThread(String title) {
        Slack slack = Slack.getInstance();

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
            .token(SLACK_BOT_TOKEN)
            .channel(SLACK_CHANNEL_ID)
            .text(title)
            .build();

        try {
            ChatPostMessageResponse response = slack.methods().chatPostMessage(request);

            if (response.isOk()) {
                String timestamp = response.getTs();
                return timestamp;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean createComment(String parentTs, String content) {
        Slack slack = Slack.getInstance();

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
            .token(SLACK_BOT_TOKEN)
            .channel(SLACK_CHANNEL_ID)
            .text(content)
            .threadTs(parentTs) // 부모 메시지의 timestamp
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
