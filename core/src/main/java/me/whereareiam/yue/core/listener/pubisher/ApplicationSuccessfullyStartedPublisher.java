package me.whereareiam.yue.core.listener.pubisher;

import me.whereareiam.yue.api.event.ApplicationSuccessfullyStarted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ApplicationSuccessfullyStartedPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	public ApplicationSuccessfullyStartedPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	public void publish() {
		applicationEventPublisher.publishEvent(new ApplicationSuccessfullyStarted());
	}
}
