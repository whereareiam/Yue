package com.aeritt.yue.listener;

import com.aeritt.yue.api.service.member.CacheReason;
import com.aeritt.yue.api.service.member.DiscordMemberService;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageReceivedListener extends ListenerAdapter {
	private final DiscordMemberService memberManager;

	@Autowired
	public MessageReceivedListener(DiscordMemberService memberManager) {
		this.memberManager = memberManager;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		memberManager.cacheMember(event.getAuthor().getId(), CacheReason.MESSAGE);
	}
}
