package com.aeritt.yue.core.discord;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Lazy
@Service
public class DiscordButtonManager implements com.aeritt.yue.api.discord.DiscordButtonManager {
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

