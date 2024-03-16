package com.aeritt.yue.core.event;

import org.springframework.context.ApplicationEvent;

public class ApplicationReloaded extends ApplicationEvent {
	public ApplicationReloaded(Object source) {
		super(source);
	}
}
