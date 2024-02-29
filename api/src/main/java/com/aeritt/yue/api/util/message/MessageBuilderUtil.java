package com.aeritt.yue.api.util.message;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.Optional;

public interface MessageBuilderUtil {
	MessageEmbed embed(String embedId, User user, Optional<PlaceholderReplacement> replacement);

	MessageEmbed.Field field(User user, String name, String value, boolean inline);

	Button button(String buttonId, User user);
}
