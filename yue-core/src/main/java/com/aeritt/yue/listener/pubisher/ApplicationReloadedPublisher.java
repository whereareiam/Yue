package com.aeritt.yue.listener.pubisher;

import com.aeritt.yue.event.ApplicationReloaded;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ApplicationReloadedPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	public ApplicationReloadedPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	public void publish() {
		applicationEventPublisher.publishEvent(new ApplicationReloaded(this));
	}
}