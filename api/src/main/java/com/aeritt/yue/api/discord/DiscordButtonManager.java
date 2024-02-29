package com.aeritt.yue.api.discord;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.function.Consumer;

public interface DiscordButtonManager {
	void addButton(String id, Consumer<ButtonInteractionEvent> action);

	void handleButton(ButtonInteractionEvent event);
}