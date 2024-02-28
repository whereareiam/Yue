package me.whereareiam.yue.api.util.message;

import net.dv8tion.jda.api.interactions.InteractionHook;

public interface MessageSenderUtil {
	void noRequiredRole(InteractionHook hook, String allowedRole);

	void noRequiredRole(InteractionHook hook, String[] allowedRole);

	void noRequiredChannel(InteractionHook hook, String[] allowedChannel);

	void noRequiredUser(InteractionHook hook, String id);

	void guildOnly(InteractionHook hook);
}
