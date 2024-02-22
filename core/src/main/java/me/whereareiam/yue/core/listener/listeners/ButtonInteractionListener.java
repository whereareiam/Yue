package me.whereareiam.yue.core.listener.listeners;

import me.whereareiam.yue.core.discord.DiscordButtonManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ButtonInteractionListener extends ListenerAdapter {
	private final DiscordButtonManager buttonManager;

	@Autowired
	public ButtonInteractionListener(DiscordButtonManager buttonManager) {
		this.buttonManager = buttonManager;
	}

	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		buttonManager.handleButton(event);
		event.getInteraction().deferEdit().queue();
	}
}
