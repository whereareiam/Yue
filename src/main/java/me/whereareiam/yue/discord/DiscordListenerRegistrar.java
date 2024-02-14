package me.whereareiam.yue.discord;

import me.whereareiam.yue.discord.listener.MessageListener;
import net.dv8tion.jda.api.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class DiscordListenerRegistrar {
	private final ApplicationContext ctx;
	private final JDA jda;

	@Autowired
	public DiscordListenerRegistrar(@Qualifier ApplicationContext ctx, JDA jda) {
		this.jda = jda;
		this.ctx = ctx;
	}

	public void registerListeners() {
		jda.addEventListener(ctx.getBean(MessageListener.class));
	}
}
