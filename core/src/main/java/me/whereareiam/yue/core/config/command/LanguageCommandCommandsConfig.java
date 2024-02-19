package me.whereareiam.yue.core.config.command;

import lombok.Getter;
import me.whereareiam.yue.core.command.base.CommandCategory;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LanguageCommandCommandsConfig {
	private boolean enabled = true;
	private List<String> command = List.of(
			"lang",
			"language"
	);
	private CommandCategory category = CommandCategory.UTILITY;
	private String description = "$t{core.commands.utility.language.description}";
	private String role = "";
	private List<String> allowedChannels = new ArrayList<>();
}
