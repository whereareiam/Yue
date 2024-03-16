package com.aeritt.yue.core.util.message;

import com.aeritt.yue.api.message.MessageFormatter;
import com.aeritt.yue.api.message.PlaceholderReplacement;
import com.aeritt.yue.api.model.CustomButton;
import com.aeritt.yue.api.model.embed.Embed;
import com.aeritt.yue.api.model.embed.EmbedFooter;
import com.aeritt.yue.api.type.ColorType;
import com.aeritt.yue.core.component.ComponentService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageBuilder implements com.aeritt.yue.api.message.MessageBuilder {
	private final MessageFormatter messageFormatter;
	private final ComponentService componentService;

	@Autowired
	public MessageBuilder(MessageFormatter messageFormatter, ComponentService componentService) {
		this.messageFormatter = messageFormatter;
		this.componentService = componentService;
	}

	public MessageEmbed embed(String embedId, User user, Optional<PlaceholderReplacement> replacement) {
		Embed embed = componentService.getEmbedComponent(embedId);

		String[] message = embed.getMessage();
		message = messageFormatter.formatMessage(user, message);

		if (replacement.isPresent())
			message = messageFormatter.replacePlaceholders(message, replacement.get());

		EmbedBuilder embedBuilder = new EmbedBuilder();

		String[] finalMessage = message;
		Optional.ofNullable(embed.getTitle()).ifPresent(title -> embedBuilder.setTitle(finalMessage[0]));
		Optional.ofNullable(embed.getDescription()).ifPresent(description -> embedBuilder.setDescription(finalMessage[1]));
		Optional.ofNullable(embed.getColor())
				.map(color -> color.equals(color.toUpperCase()) ? ColorType.valueOf(color) : color)
				.map(color -> color instanceof ColorType ?
						componentService.getColorComponent((ColorType) color) :
						componentService.getColorComponent((String) color))
				.ifPresent(embedBuilder::setColor);

		Optional.ofNullable(embed.getUrl()).ifPresent(embedBuilder::setUrl);
		Optional.ofNullable(embed.getImage()).ifPresent(embedBuilder::setImage);
		Optional.ofNullable(embed.getThumbnail()).ifPresent(embedBuilder::setThumbnail);

		//Author
		Optional.ofNullable(embed.getAuthor()).ifPresent(author -> {
			String url = Optional.ofNullable(author.getUrl()).orElse(null);
			String iconUrl = Optional.ofNullable(author.getIconUrl()).orElse(null);
			embedBuilder.setAuthor(author.getName(), url, iconUrl);
		});

		//Footer
		Optional.ofNullable(embed.getFooter()).map(EmbedFooter::getText).ifPresent(footerText -> {
			String iconUrl = Optional.ofNullable(embed.getFooter().getIconUrl()).orElse(null);
			embedBuilder.setFooter(finalMessage[finalMessage.length - 1], iconUrl);
		});

		//Fields
		embed.getFields().forEach(field -> {
			String fieldName = messageFormatter.formatMessage(user, field.getName());
			String fieldValue = messageFormatter.formatMessage(user, field.getValue());
			if (replacement.isPresent()) {
				fieldName = messageFormatter.replacePlaceholders(fieldName, replacement.get());
				fieldValue = messageFormatter.replacePlaceholders(fieldValue, replacement.get());
			}

			embedBuilder.addField(fieldName, fieldValue, field.isInline());
		});

		return embedBuilder.build();
	}

	public MessageEmbed.Field field(User user, String name, String value, boolean inline) {
		name = messageFormatter.formatMessage(user, name);
		value = messageFormatter.formatMessage(user, value);

		return new MessageEmbed.Field(name, value, inline);
	}

	public Button button(String buttonId, User user) {
		CustomButton customButton = componentService.getButtonComponent(buttonId);

		String label = customButton.getLabel();
		label = messageFormatter.formatMessage(user, label);

		return Button.of(customButton.getStyle(), buttonId, label);
	}
}
