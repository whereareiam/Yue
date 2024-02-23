package me.whereareiam.yue.api;

import me.whereareiam.yue.api.command.management.CommandRegistrar;
import me.whereareiam.yue.api.config.ConfigService;
import me.whereareiam.yue.api.language.LanguageService;
import me.whereareiam.yue.api.service.PersonLanguageService;
import me.whereareiam.yue.api.service.PersonRoleService;
import me.whereareiam.yue.api.service.PersonService;

public interface Yue {
	CommandRegistrar getCommandRegistrar();

	ConfigService getConfigService();

	LanguageService getLanguageService();

	PersonLanguageService getPersonLanguageService();

	PersonRoleService getPersonRoleService();

	PersonService getPersonService();
}