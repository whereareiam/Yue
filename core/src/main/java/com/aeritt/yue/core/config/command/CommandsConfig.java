package com.aeritt.yue.core.config.command;

import lombok.Getter;

@Getter
public class CommandsConfig {
	private DeleteCommandConfig deleteCommand = new DeleteCommandConfig();
	private PluginCommandConfig pluginCommand = new PluginCommandConfig();
}
