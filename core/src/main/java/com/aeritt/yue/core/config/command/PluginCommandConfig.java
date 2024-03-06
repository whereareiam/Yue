package com.aeritt.yue.core.config.command;

import com.aeritt.yue.api.command.base.CommandCategory;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PluginCommandConfig {
	private boolean enabled = true;
	private List<String> command = List.of(
			"plugin",
			"plugins"
	);
	private CommandCategory category = CommandCategory.STAFF;
	private String shortDescription = "$t{core.commands.staff.plugin.shortDescription}";
	private String fullDescription = "$t{core.commands.staff.plugin.fullDescription}";
	private String roleId = "1209471775805800478";
	private List<String> allowedChannels = new ArrayList<>();
}
