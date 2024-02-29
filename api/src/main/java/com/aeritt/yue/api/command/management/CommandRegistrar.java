package com.aeritt.yue.api.command.management;

import com.aeritt.yue.api.command.base.CommandBase;

import java.util.Set;

public interface CommandRegistrar {
	void registerCommand(CommandBase commandBase);

	void unregisterCommand(CommandBase commandBase);

	Set<CommandBase> getCommands();
}
