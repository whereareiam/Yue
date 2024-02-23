package me.whereareiam.yue.core;

import me.whereareiam.yue.api.command.management.CommandRegistrar;
import me.whereareiam.yue.api.config.ConfigService;
import me.whereareiam.yue.api.language.LanguageService;
import me.whereareiam.yue.api.service.PersonLanguageService;
import me.whereareiam.yue.api.service.PersonRoleService;
import me.whereareiam.yue.api.service.PersonService;
import org.springframework.context.ApplicationContext;

public abstract class Yue implements me.whereareiam.yue.api.Yue {
	private final ApplicationContext ctx;

	public Yue(ApplicationContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public CommandRegistrar getCommandRegistrar() {
		return ctx.getBean(CommandRegistrar.class);
	}

	@Override
	public ConfigService getConfigService() {
		return ctx.getBean(ConfigService.class);
	}

	@Override
	public LanguageService getLanguageService() {
		return ctx.getBean(LanguageService.class);
	}

	@Override
	public PersonLanguageService getPersonLanguageService() {
		return ctx.getBean(PersonLanguageService.class);
	}

	@Override
	public PersonRoleService getPersonRoleService() {
		return ctx.getBean(PersonRoleService.class);
	}

	@Override
	public PersonService getPersonService() {
		return ctx.getBean(PersonService.class);
	}
}
