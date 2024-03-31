package com.aeritt.yue.listener.registry;

import com.aeritt.yue.listener.ButtonInteractionListener;
import com.aeritt.yue.listener.MessageReceivedListener;
import com.aeritt.yue.listener.ReadyListener;
import com.aeritt.yue.listener.SlashCommandInteractionListener;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Lazy
@Service
@DependsOn("discordSetupService")
public class ListenerRegistrar {
	private final ApplicationContext ctx;
	private final JDA jda;

	@Autowired
	public ListenerRegistrar(@Qualifier ApplicationContext ctx, @Lazy JDA jda) {
		this.jda = jda;
		this.ctx = ctx;
	}

	@Async
	@Order(Ordered.HIGHEST_PRECEDENCE + 1)
	@EventListener(ApplicationReadyEvent.class)
	public void registerListeners() {
		jda.addEventListener(ctx.getBean(SlashCommandInteractionListener.class));
		jda.addEventListener(ctx.getBean(ButtonInteractionListener.class));
		jda.addEventListener(ctx.getBean(MessageReceivedListener.class));
		jda.addEventListener(ctx.getBean(ReadyListener.class));
	}
}
