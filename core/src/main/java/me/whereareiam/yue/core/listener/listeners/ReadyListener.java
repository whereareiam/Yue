package me.whereareiam.yue.core.listener.listeners;

import me.whereareiam.yue.core.listener.pubisher.ApplicationSuccessfullyStartedPublisher;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReadyListener extends ListenerAdapter {
	private final ApplicationSuccessfullyStartedPublisher eventPublisher;

	@Autowired
	public ReadyListener(ApplicationSuccessfullyStartedPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void onReady(ReadyEvent event) {
		eventPublisher.publish();
	}
}
