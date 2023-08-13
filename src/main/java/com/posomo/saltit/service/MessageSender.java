package com.posomo.saltit.service;

import com.posomo.saltit.domain.exception.SlackMessageException;

public interface MessageSender {
	void sendMessage(String channel, String text) throws SlackMessageException;

	void sendMessage(String text) throws SlackMessageException;

	void setToken(String token);
}
