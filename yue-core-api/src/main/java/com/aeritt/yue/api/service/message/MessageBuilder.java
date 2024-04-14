package com.aeritt.yue.api.service.message;

import com.aeritt.yue.api.model.language.Placeholder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public interface MessageBuilder {
	MessageEmbed embed(String embedId, User user, List<Placeholder> replacement);

	MessageEmbed.Field field(User user, String name, String value, boolean inline);

	Button button(String buttonId, User user);
}
