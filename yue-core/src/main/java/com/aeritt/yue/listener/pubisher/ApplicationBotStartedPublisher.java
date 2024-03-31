package com.aeritt.yue.listener.pubisher;

import com.aeritt.yue.event.ApplicationBotStarted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ApplicationBotStartedPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	public ApplicationBotStartedPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	public void publish() {
		applicationEventPublisher.publishEvent(new ApplicationBotStarted(this));
	}
}