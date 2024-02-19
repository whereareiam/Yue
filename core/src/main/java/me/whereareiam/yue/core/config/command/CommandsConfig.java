package me.whereareiam.yue.core.config.command;

import lombok.Getter;

@Getter
public class CommandsConfig {
	private HelpCommandCommandsConfig helpCommand = new HelpCommandCommandsConfig();
	private LanguageCommandCommandsConfig languageCommand = new LanguageCommandCommandsConfig();
}
