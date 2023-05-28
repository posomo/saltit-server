package com.posomo.saltit.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.posomo.saltit.domain.exception.SlackMessageException;

@SpringBootTest
class SlackMessageSenderTest {

	@Autowired
	SlackMessageSender slackMessageSender;


	@DisplayName("Slack에 메시지 보내기 테스트")
	@Nested
	class sendMessage {
		@DisplayName("성공 케이스")
		@Test
		void ok() {
			Assertions.assertDoesNotThrow(() -> slackMessageSender.sendMessage("testMessage"));
		}

		@DisplayName("잘못된 토큰 사용")
		@Test
		void wrongToken() {
			slackMessageSender.setToken("wrong_token");
			Assertions.assertThrows(SlackMessageException.class, () -> slackMessageSender.sendMessage("wrong_token_test"));
			try {
				slackMessageSender.sendMessage("wrong_token_test");
			} catch (SlackMessageException e) {
				assertThat(e.getMessage()).isEqualTo("invalid_auth");
			}
		}

		@DisplayName("잘못된 채널로 메시지 전송")
		@Test
		void wrongChannel() {
			Assertions.assertThrows(SlackMessageException.class, () -> slackMessageSender.sendMessage("wrong_channel_test1", "wrong_channel_test"));
			try {
				slackMessageSender.sendMessage("wrong_channel_test1", "wrong_channel_test");
			} catch (SlackMessageException e) {
				System.out.println(e.getMessage());
				assertThat(e.getMessage()).isEqualTo("channel_not_found");
			}
		}
	}

}
