package me.whereareiam.yue.core.config.command;

import lombok.Getter;
import me.whereareiam.yue.core.command.base.CommandCategory;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HelpCommandCommandsConfig {
	private boolean enabled = true;
	private List<String> command = List.of(
			"help",
			"helpme"
	);
	private CommandCategory category = CommandCategory.GENERAL;
	private String description = "$t{core.commands.general.help.description}";
	private String commandOption = "$t{core.commands.general.help.options.command.description}";
	private String role = "";
	private List<String> allowedChannels = new ArrayList<>();

}
