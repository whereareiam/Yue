package me.whereareiam.yue.core.util.message;

import me.whereareiam.yue.core.config.component.ButtonsConfig;
import me.whereareiam.yue.core.config.component.EmbedsConfig;
import me.whereareiam.yue.core.config.component.palette.PaletteConfig;
import me.whereareiam.yue.core.model.CustomButton;
import me.whereareiam.yue.core.model.Embed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Optional;

@Service
public class MessageBuilderUtil {
	private static PaletteConfig paletteConfig;
	private static ButtonsConfig buttonsConfig;
	private static EmbedsConfig embedsConfig;

	@Autowired
	public MessageBuilderUtil(PaletteConfig paletteConfig, ButtonsConfig buttonsConfig, EmbedsConfig embedsConfig) {
		MessageBuilderUtil.paletteConfig = paletteConfig;
		MessageBuilderUtil.buttonsConfig = buttonsConfig;
		MessageBuilderUtil.embedsConfig = embedsConfig;
	}

	public static MessageEmbed embed(String embedId, User user, Optional<PlaceholderReplacement> replacement) {
		Embed embed = embedsConfig.getEmbeds().stream()
				.filter(e -> e.getId().equals(embedId))
				.findFirst()
				.orElseThrow();

		String[] message = embed.getMessage();
		message = MessageFormatterUtil.formatMessage(user, message);

		if (replacement.isPresent())
			message = MessageFormatterUtil.replacePlaceholders(message, replacement.get());

		EmbedBuilder embedBuilder = new EmbedBuilder();
		if (embed.getTitle() != null) embedBuilder.setTitle(message[0]);
		if (embed.getDescription() != null) embedBuilder.setDescription(message[1]);
		if (embed.getFooter() != null) embedBuilder.setFooter(message[2]);
		if (embed.getColor() != null) embedBuilder.setColor(Color.decode(paletteConfig.getColor(embed.getColor())));
		if (!embed.getFields().isEmpty()) {
			embed.getFields().forEach(field -> {
				String fieldValue = MessageFormatterUtil.formatMessage(user, field.getValue());
				if (replacement.isPresent())
					fieldValue = MessageFormatterUtil.replacePlaceholders(fieldValue, replacement.get());

				embedBuilder.addField(field.getName(), fieldValue, field.isInline());
			});
		}

		return embedBuilder.build();
	}

	public static MessageEmbed.Field field(User user, String name, String value, boolean inline) {
		name = MessageFormatterUtil.formatMessage(user, name);
		value = MessageFormatterUtil.formatMessage(user, value);

		return new MessageEmbed.Field(name, value, inline);
	}

	public static Button button(String buttonId, User user) {
		CustomButton customButton = buttonsConfig.getButtons().stream()
				.filter(button -> button.getId().equals(buttonId))
				.findFirst()
				.orElseThrow();

		String label = customButton.getLabel();
		label = MessageFormatterUtil.formatMessage(user, label);

		return Button.of(customButton.getStyle(), customButton.getId(), label);
	}
}
