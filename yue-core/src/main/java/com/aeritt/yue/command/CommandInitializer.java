package com.aeritt.yue.command;

import com.aeritt.yue.api.command.base.CommandBase;
import com.aeritt.yue.command.management.CommandRegistrar;
import com.aeritt.yue.event.ApplicationBotStarted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CommandInitializer {
	private final CommandRegistrar commandRegistrar;
	private final ApplicationContext ctx;

	@Autowired
	public CommandInitializer(CommandRegistrar commandRegistrar, @Qualifier ApplicationContext ctx) {
		this.commandRegistrar = commandRegistrar;
		this.ctx = ctx;
	}

	@Async
	@Order(Ordered.HIGHEST_PRECEDENCE)
	@EventListener(ApplicationBotStarted.class)
	public void initialize() {
		ctx.getBeansOfType(CommandBase.class).values().forEach(commandRegistrar::registerCommand);
	}
}
