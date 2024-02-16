package me.whereareiam.yue.core.listener.pubisher;

import me.whereareiam.yue.api.event.ApplicationSuccessfullyStarted;
import me.whereareiam.yue.core.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ApplicationSuccessfullyStartedPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;
	private final Scheduler scheduler;

	@Autowired
	public ApplicationSuccessfullyStartedPublisher(ApplicationEventPublisher applicationEventPublisher, Scheduler scheduler) {
		this.applicationEventPublisher = applicationEventPublisher;
		this.scheduler = scheduler;
	}

	public void publish() {
		scheduler.schedule(() -> applicationEventPublisher.publishEvent(new ApplicationSuccessfullyStarted()), 1, TimeUnit.SECONDS);
	}
}
