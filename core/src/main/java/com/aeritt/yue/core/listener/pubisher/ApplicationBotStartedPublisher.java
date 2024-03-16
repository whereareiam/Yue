package com.aeritt.yue.core.listener.pubisher;

import com.aeritt.yue.api.Scheduler;
import com.aeritt.yue.core.event.ApplicationBotStarted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ApplicationBotStartedPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;
	private final Scheduler scheduler;

	@Autowired
	public ApplicationBotStartedPublisher(ApplicationEventPublisher applicationEventPublisher, Scheduler scheduler) {
		this.applicationEventPublisher = applicationEventPublisher;
		this.scheduler = scheduler;
	}

	public void publish() {
		scheduler.schedule(() -> applicationEventPublisher.publishEvent(new ApplicationBotStarted(this)), 1, TimeUnit.SECONDS);
	}
}
