package com.aeritt.yue.config.command;

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
	private String shortDescription = "$t{core.commands.categories.staff.delete.shortDescription}";
	private String fullDescription = "$t{core.commands.categories.staff.delete.fullDescription}";
	private String roleId = "1209471775805800478";
	private String embedId = "core.embed.commands.delete.success";
	private List<String> allowedChannels = new ArrayList<>();
}