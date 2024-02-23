package me.whereareiam.yue.core.config.command;

import lombok.Getter;
import me.whereareiam.yue.api.command.base.CommandCategory;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InfoCommandCommandsConfig {
	private boolean enabled = true;
	private List<String> command = List.of(
			"info",
			"botinfo"
	);
	private CommandCategory category = CommandCategory.UTILITY;
	private String shortDescription = "$t{core.commands.utility.info.shortDescription}";
	private String fullDescription = "$t{core.commands.utility.info.fullDescription}";
	private String role = "1209471775805800478";
	private List<String> allowedChannels = new ArrayList<>();
}
