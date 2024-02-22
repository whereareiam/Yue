package me.whereareiam.yue.core.listener.pubisher;

import me.whereareiam.yue.api.event.ApplicationBotStarted;
import me.whereareiam.yue.core.Scheduler;
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
		scheduler.schedule(() -> applicationEventPublisher.publishEvent(new ApplicationBotStarted()), 1, TimeUnit.SECONDS);
	}
}
