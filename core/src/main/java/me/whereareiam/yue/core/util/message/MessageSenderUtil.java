package me.whereareiam.yue.core.util.message;

import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageSenderUtil {
	public static void noRequiredRole(InteractionHook hook, String allowedRole) {
		noRequiredRole(hook, new String[]{allowedRole});
	}

	public static void noRequiredRole(InteractionHook hook, String[] allowedRole) {
		String allowedRoles = String.join(", ", allowedRole);

		hook.sendMessageEmbeds(
				MessageBuilderUtil.embed(
						"noRequiredRole",
						hook.getInteraction().getUser(),
						Optional.of(new PlaceholderReplacement(List.of("{roleMention}"), List.of(allowedRoles)))
				)
		).queue();
	}

	public static void noRequiredChannel(InteractionHook hook, String[] allowedChannel) {
		String allowedChannels = String.join(", ", allowedChannel);

		hook.sendMessageEmbeds(
				MessageBuilderUtil.embed(
						"noRequiredChannel",
						hook.getInteraction().getUser(),
						Optional.of(new PlaceholderReplacement(List.of("{channelMention}"), List.of(allowedChannels)))
				)
		).queue();
	}

	public static void noRequiredUser(InteractionHook hook, String id) {
		hook.sendMessageEmbeds(
				MessageBuilderUtil.embed(
						"noRequiredUser",
						hook.getInteraction().getUser(),
						Optional.of(new PlaceholderReplacement(List.of("{userMention}"), List.of(id)))
				)
		).queue();
	}

	public static void guildOnly(InteractionHook hook) {
		hook.sendMessageEmbeds(
				MessageBuilderUtil.embed(
						"guildOnly",
						hook.getInteraction().getUser(),
						Optional.empty()
				)
		).queue();
	}
}
