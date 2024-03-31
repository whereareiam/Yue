package com.aeritt.yue.listener;

import com.aeritt.yue.listener.pubisher.ApplicationBotStartedPublisher;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReadyListener extends ListenerAdapter {
	private final ApplicationBotStartedPublisher eventPublisher;

	@Autowired
	public ReadyListener(ApplicationBotStartedPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void onReady(@NotNull ReadyEvent event) {
		eventPublisher.publish();
	}
}
