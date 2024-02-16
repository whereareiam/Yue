package me.whereareiam.yue.core.listener;

import me.whereareiam.yue.core.listener.listeners.MessageListener;
import me.whereareiam.yue.core.listener.listeners.ReadyListener;
import me.whereareiam.yue.core.listener.listeners.SlashCommandInteractionListener;
import me.whereareiam.yue.core.listener.listeners.guild.GuildMemberJoinListener;
import me.whereareiam.yue.core.listener.listeners.guild.GuildMemberRemoveListener;
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
		jda.addEventListener(ctx.getBean(GuildMemberRemoveListener.class));
		jda.addEventListener(ctx.getBean(ReadyListener.class));
	}
}
