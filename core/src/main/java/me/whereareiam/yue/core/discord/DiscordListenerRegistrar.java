package me.whereareiam.yue.core.discord;

import me.whereareiam.yue.core.listener.listeners.discord.GuildMemberJoinListener;
import me.whereareiam.yue.core.listener.listeners.discord.MessageListener;
import me.whereareiam.yue.core.listener.listeners.discord.SlashCommandInteractionListener;
import net.dv8tion.jda.api.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class DiscordListenerRegistrar {
	private final ApplicationContext ctx;
	private final JDA jda;

	@Autowired
	public DiscordListenerRegistrar(@Qualifier ApplicationContext ctx, @Lazy JDA jda) {
		this.jda = jda;
		this.ctx = ctx;
	}

	public void registerListeners() {
		jda.addEventListener(ctx.getBean(MessageListener.class));
		jda.addEventListener(ctx.getBean(SlashCommandInteractionListener.class));
		jda.addEventListener(ctx.getBean(GuildMemberJoinListener.class));
	}
}
