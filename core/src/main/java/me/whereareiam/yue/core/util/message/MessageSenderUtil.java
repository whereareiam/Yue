package me.whereareiam.yue.core.util.message;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class MessageSenderUtil {
	private final MessageBuilderUtil builderUtil;
	private final Guild guild;

	@Autowired
	public MessageSenderUtil(MessageBuilderUtil builderUtil, @Lazy Guild guild) {
		this.builderUtil = builderUtil;
		this.guild = guild;
	}

	public void noRequiredRole(InteractionHook hook, String allowedRole) {
		hook.sendMessageEmbeds(
				builderUtil.dangerEmbed(hook.getInteraction().getUser(),
						"core.commands.messages.error.title",
						"core.commands.messages.error.noRequiredRole",
						"core.commands.messages.error.footer",
						allowedRole
				)
		).queue();
	}

	public void noRequiredChannel(InteractionHook hook, String... allowedChannels) {
		hook.sendMessageEmbeds(
				builderUtil.dangerEmbed(hook.getInteraction().getUser(),
						"core.commands.messages.error.title",
						"core.commands.messages.error.noRequiredChannel",
						"core.commands.messages.error.footer",
						allowedChannels
				)
		).queue();
	}

	public void guildOnly(InteractionHook hook) {
		hook.sendMessageEmbeds(
				builderUtil.dangerEmbed(hook.getInteraction().getUser(),
						"core.commands.messages.error.title",
						"core.commands.messages.error.guildOnly",
						"core.commands.messages.error.footer"
				)
		).queue();
	}
}
