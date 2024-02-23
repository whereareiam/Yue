package me.whereareiam.yue.core.config.command;

import lombok.Getter;
import me.whereareiam.yue.api.command.base.CommandCategory;

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
	private String shortDescription = "$t{core.commands.general.help.shortDescription}";
	private String fullDescription = "$t{core.commands.general.help.fullDescription}";
	private String role = "";
	private List<String> allowedChannels = new ArrayList<>();

}
