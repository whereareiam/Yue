package me.whereareiam.yue.api.event;

import org.springframework.context.ApplicationEvent;

public class ApplicationBotStarted extends ApplicationEvent {
	public ApplicationBotStarted(Object source) {
		super(source);
	}
}
