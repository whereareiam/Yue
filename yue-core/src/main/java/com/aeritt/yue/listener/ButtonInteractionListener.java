package com.aeritt.yue.listener;

import com.aeritt.yue.api.service.member.CacheReason;
import com.aeritt.yue.api.service.member.DiscordMemberService;
import com.aeritt.yue.listener.pubisher.ApplicationReloadedPublisher;
import com.aeritt.yue.service.DiscordButtonService;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ButtonInteractionListener extends ListenerAdapter {
	private final DiscordMemberService memberManager;
	private final DiscordButtonService buttonManager;

	@Autowired
	private ApplicationReloadedPublisher applicationReloadedPublisher;

	@Autowired
	public ButtonInteractionListener(DiscordMemberService memberManager, DiscordButtonService buttonManager) {
		this.memberManager = memberManager;
		this.buttonManager = buttonManager;
	}

	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		memberManager.cacheMember(event.getUser().getId(), CacheReason.BUTTON);
		buttonManager.handleButton(event);
		event.getInteraction().deferEdit().queue();

		applicationReloadedPublisher.publish();
	}
}
