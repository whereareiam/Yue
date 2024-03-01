package com.aeritt.yue.core.config.command;

import com.aeritt.yue.api.command.base.CommandCategory;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DeleteCommandConfig {
	private boolean enabled = true;
	private List<String> command = List.of(
			"delete",
			"deleteuser"
	);
	private CommandCategory category = CommandCategory.UTILITY;
	private String shortDescription = "$t{core.commands.staff.delete.shortDescription}";
	private String fullDescription = "$t{core.commands.staff.delete.fullDescription}";
	private String role = "1209471775805800478";
	private List<String> allowedChannels = new ArrayList<>();
}
