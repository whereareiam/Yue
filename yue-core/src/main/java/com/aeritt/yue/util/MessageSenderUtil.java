package com.aeritt.yue.util;

import com.aeritt.yue.api.model.language.Placeholder;
import com.aeritt.yue.service.message.MessageBuilder;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageSenderUtil {
	private final MessageBuilder messageBuilder;

	@Autowired
	public MessageSenderUtil(MessageBuilder messageBuilder) {
		this.messageBuilder = messageBuilder;
	}

	public void noRequiredRole(InteractionHook hook, String allowedRole) {
		noRequiredRole(hook, new String[]{allowedRole});
	}

	public void noRequiredRole(InteractionHook hook, String[] allowedRole) {
		String allowedRoles = String.join(", ", allowedRole);

		hook.sendMessageEmbeds(
				messageBuilder.embed(
						"core.embed.default.errors.noRequiredRole",
						hook.getInteraction().getUser(),
						List.of(new Placeholder("{roleMention}", allowedRoles))
				)
		).queue();
	}

	public void noRequiredChannel(InteractionHook hook, String[] allowedChannel) {
		String allowedChannels = String.join(", ", allowedChannel);

		hook.sendMessageEmbeds(
				messageBuilder.embed(
						"core.embed.default.errors.noRequiredChannel",
						hook.getInteraction().getUser(),
						List.of(new Placeholder("{channelMention}", allowedChannels))
				)
		).queue();
	}

	public void noRequiredUser(InteractionHook hook, String id) {
		hook.sendMessageEmbeds(
				messageBuilder.embed(
						"core.embed.default.errors.noRequiredUser",
						hook.getInteraction().getUser(),
						List.of(new Placeholder("{userMention}", id))
				)
		).queue();
	}

	public void guildOnly(InteractionHook hook) {
		hook.sendMessageEmbeds(
				messageBuilder.embed(
						"core.embed.default.errors.guildOnly",
						hook.getInteraction().getUser(),
						List.of()
				)
		).queue();
	}
}
