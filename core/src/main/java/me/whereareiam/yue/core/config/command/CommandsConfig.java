package me.whereareiam.yue.core.config.command;

import lombok.Getter;

@Getter
public class CommandsConfig {
	private HelpCommandCommandsConfig helpCommand = new HelpCommandCommandsConfig();
	private DeleteCommandCommandsConfig deleteCommand = new DeleteCommandCommandsConfig();
	private InfoCommandCommandsConfig infoCommand = new InfoCommandCommandsConfig();
}
