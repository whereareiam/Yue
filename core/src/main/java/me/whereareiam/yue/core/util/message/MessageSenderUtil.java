package me.whereareiam.yue.core.util.message;

import me.whereareiam.yue.api.util.message.PlaceholderReplacement;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageSenderUtil implements me.whereareiam.yue.api.util.message.MessageSenderUtil {
	private final MessageBuilderUtil messageBuilderUtil;

	@Autowired
	public MessageSenderUtil(MessageBuilderUtil messageBuilderUtil) {
		this.messageBuilderUtil = messageBuilderUtil;
	}

	public void noRequiredRole(InteractionHook hook, String allowedRole) {
		noRequiredRole(hook, new String[]{allowedRole});
	}

	public void noRequiredRole(InteractionHook hook, String[] allowedRole) {
		String allowedRoles = String.join(", ", allowedRole);

		hook.sendMessageEmbeds(
				messageBuilderUtil.embed(
						"noRequiredRole",
						hook.getInteraction().getUser(),
						Optional.of(new PlaceholderReplacement(List.of("{roleMention}"), List.of(allowedRoles)))
				)
		).queue();
	}

	public void noRequiredChannel(InteractionHook hook, String[] allowedChannel) {
		String allowedChannels = String.join(", ", allowedChannel);

		hook.sendMessageEmbeds(
				messageBuilderUtil.embed(
						"noRequiredChannel",
						hook.getInteraction().getUser(),
						Optional.of(new PlaceholderReplacement(List.of("{channelMention}"), List.of(allowedChannels)))
				)
		).queue();
	}

	public void noRequiredUser(InteractionHook hook, String id) {
		hook.sendMessageEmbeds(
				messageBuilderUtil.embed(
						"noRequiredUser",
						hook.getInteraction().getUser(),
						Optional.of(new PlaceholderReplacement(List.of("{userMention}"), List.of(id)))
				)
		).queue();
	}

	public void guildOnly(InteractionHook hook) {
		hook.sendMessageEmbeds(
				messageBuilderUtil.embed(
						"guildOnly",
						hook.getInteraction().getUser(),
						Optional.empty()
				)
		).queue();
	}
}
