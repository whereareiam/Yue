package com.aeritt.yue.core.listener;

import com.aeritt.yue.core.listener.listeners.ButtonInteractionListener;
import com.aeritt.yue.core.listener.listeners.MessageListener;
import com.aeritt.yue.core.listener.listeners.ReadyListener;
import com.aeritt.yue.core.listener.listeners.SlashCommandInteractionListener;
import com.aeritt.yue.core.listener.listeners.guild.GuildMemberJoinListener;
import com.aeritt.yue.core.listener.listeners.guild.GuildMemberRemoveListener;
import net.dv8tion.jda.api.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Lazy
@Service
@DependsOn("discordSetupManager")
public class DiscordListenerRegistrar {
	private final ApplicationContext ctx;
	private final JDA jda;

	@Autowired
	public DiscordListenerRegistrar(@Qualifier ApplicationContext ctx, @Lazy JDA jda) {
		this.jda = jda;
		this.ctx = ctx;
	}

	@Order(Ordered.HIGHEST_PRECEDENCE + 1)
	@EventListener(ApplicationReadyEvent.class)
	public void registerListeners() {
		jda.addEventListener(ctx.getBean(MessageListener.class));
		jda.addEventListener(ctx.getBean(SlashCommandInteractionListener.class));
		jda.addEventListener(ctx.getBean(GuildMemberJoinListener.class));
		jda.addEventListener(ctx.getBean(GuildMemberRemoveListener.class));
		jda.addEventListener(ctx.getBean(ButtonInteractionListener.class));
		jda.addEventListener(ctx.getBean(ReadyListener.class));
	}
}
