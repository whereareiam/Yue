package me.whereareiam.yue.core.util.message;

import me.whereareiam.yue.api.model.CustomButton;
import me.whereareiam.yue.api.model.Embed;
import me.whereareiam.yue.api.util.message.MessageFormatterUtil;
import me.whereareiam.yue.api.util.message.PlaceholderReplacement;
import me.whereareiam.yue.core.config.component.ComponentService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageBuilderUtil implements me.whereareiam.yue.api.util.message.MessageBuilderUtil {
	private final MessageFormatterUtil messageFormatterUtil;
	private final ComponentService componentService;

	@Autowired
	public MessageBuilderUtil(MessageFormatterUtil messageFormatterUtil, ComponentService componentService) {
		this.messageFormatterUtil = messageFormatterUtil;
		this.componentService = componentService;
	}

	public MessageEmbed embed(String embedId, User user, Optional<PlaceholderReplacement> replacement) {
		Embed embed = componentService.getEmbedComponent(embedId);

		String[] message = embed.getMessage();
		message = messageFormatterUtil.formatMessage(user, message);

		if (replacement.isPresent())
			message = messageFormatterUtil.replacePlaceholders(message, replacement.get());

		EmbedBuilder embedBuilder = new EmbedBuilder();
		if (embed.getTitle() != null) embedBuilder.setTitle(message[0]);
		if (embed.getDescription() != null) embedBuilder.setDescription(message[1]);
		if (embed.getFooter() != null) embedBuilder.setFooter(message[2]);
		if (embed.getColor() != null) {
			// TODO CustomColor parsing
		}
		if (!embed.getFields().isEmpty()) {
			embed.getFields().forEach(field -> {
				String fieldName = messageFormatterUtil.formatMessage(user, field.getName());
				String fieldValue = messageFormatterUtil.formatMessage(user, field.getValue());
				if (replacement.isPresent()) {
					fieldName = messageFormatterUtil.replacePlaceholders(fieldName, replacement.get());
					fieldValue = messageFormatterUtil.replacePlaceholders(fieldValue, replacement.get());
				}

				embedBuilder.addField(fieldName, fieldValue, field.isInline());
			});
		}

		return embedBuilder.build();
	}

	public MessageEmbed.Field field(User user, String name, String value, boolean inline) {
		name = messageFormatterUtil.formatMessage(user, name);
		value = messageFormatterUtil.formatMessage(user, value);

		return new MessageEmbed.Field(name, value, inline);
	}

	public Button button(String buttonId, User user) {
		CustomButton customButton = componentService.getButtonComponent(buttonId);

		String label = customButton.getLabel();
		label = messageFormatterUtil.formatMessage(user, label);

		return Button.of(customButton.getStyle(), customButton.getId(), label);
	}
}
