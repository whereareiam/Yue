package com.aeritt.yue.api.service;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.function.Consumer;

public interface DiscordButtonService {
	void addButton(String id, Consumer<ButtonInteractionEvent> action);

	void handleButton(ButtonInteractionEvent event);
}