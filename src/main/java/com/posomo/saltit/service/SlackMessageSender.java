package com.posomo.saltit.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.posomo.saltit.domain.exception.SlackMessageException;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

@Service
public class SlackMessageSender implements MessageSender {

	@Value("${slack.bot.token}")
	private String token;

	@Value("${slack.bot.default-channel}")
	private String channel;

	@Override
	public void sendMessage(String channel, String text) throws SlackMessageException {
		try {
			ChatPostMessageResponse response = Slack.getInstance().methods()
				.chatPostMessage(req -> req.token(token).channel(channel).text(text));
			if (!response.isOk()) {
				String errorCode = response.getError();
				throw new SlackMessageException(errorCode);
			}
		} catch (SlackApiException | IOException e) {
			throw new SlackMessageException(e.getMessage(), e);
		}
	}

	@Override
	public void sendMessage(String text) throws SlackMessageException{
		sendMessage(this.channel, text);
	}

	@Override
	public void setToken(String token) {
		this.token = token;
	}
}
