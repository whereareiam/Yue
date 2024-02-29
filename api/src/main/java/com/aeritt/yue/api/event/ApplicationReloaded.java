package com.aeritt.yue.api.event;

import org.springframework.context.ApplicationEvent;

public class ApplicationReloaded extends ApplicationEvent {
	public ApplicationReloaded(Object source) {
		super(source);
	}
}
