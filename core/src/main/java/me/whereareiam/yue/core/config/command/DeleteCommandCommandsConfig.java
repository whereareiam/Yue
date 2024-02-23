package me.whereareiam.yue.core.config.command;

import lombok.Getter;
import me.whereareiam.yue.api.command.base.CommandCategory;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DeleteCommandCommandsConfig {
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
