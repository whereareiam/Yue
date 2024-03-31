package com.aeritt.yue.service;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class DiscordButtonService implements com.aeritt.yue.api.service.DiscordButtonService {
	private final Map<String, Consumer<ButtonInteractionEvent>> buttonActions = new HashMap<>();

	public void addButton(String id, Consumer<ButtonInteractionEvent> action) {
		if (buttonActions.containsKey(id)) return;

		buttonActions.put(id, action);
	}

	public void handleButton(ButtonInteractionEvent event) {
		Consumer<ButtonInteractionEvent> action = buttonActions.get(event.getComponentId());
		if (action != null) {
			action.accept(event);
		}
	}
}