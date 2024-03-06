package com.aeritt.yue.core.listener.listeners;

import com.aeritt.yue.api.discord.member.CacheReason;
import com.aeritt.yue.api.discord.member.DiscordMemberManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageReceivedListener extends ListenerAdapter {
	private final DiscordMemberManager memberManager;

	@Autowired
	public MessageReceivedListener(DiscordMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		memberManager.cacheMember(event.getAuthor().getId(), CacheReason.MESSAGE);
	}
}
