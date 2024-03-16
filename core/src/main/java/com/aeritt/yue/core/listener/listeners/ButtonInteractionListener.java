package com.aeritt.yue.core.listener.listeners;

import com.aeritt.yue.api.discord.member.CacheReason;
import com.aeritt.yue.api.discord.member.DiscordMemberManager;
import com.aeritt.yue.core.discord.DiscordButtonManager;
import com.aeritt.yue.core.listener.pubisher.ApplicationReloadedPublisher;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ButtonInteractionListener extends ListenerAdapter {
	private final DiscordMemberManager memberManager;
	private final DiscordButtonManager buttonManager;

	@Autowired
	private ApplicationReloadedPublisher applicationReloadedPublisher;

	@Autowired
	public ButtonInteractionListener(DiscordMemberManager memberManager, DiscordButtonManager buttonManager) {
		this.memberManager = memberManager;
		this.buttonManager = buttonManager;
	}

	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		memberManager.cacheMember(event.getUser().getId(), CacheReason.BUTTON);
		buttonManager.handleButton(event);
		event.getInteraction().deferEdit().queue();

		System.out.println("pressed");
		applicationReloadedPublisher.publish();
	}
}
