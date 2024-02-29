package com.aeritt.yue.core.util.message;

import com.aeritt.yue.api.model.CustomButton;
import com.aeritt.yue.api.model.embed.Embed;
import com.aeritt.yue.api.model.embed.EmbedFooter;
import com.aeritt.yue.api.util.message.MessageFormatterUtil;
import com.aeritt.yue.api.util.message.PlaceholderReplacement;
import com.aeritt.yue.core.component.ComponentService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageBuilderUtil implements com.aeritt.yue.api.util.message.MessageBuilderUtil {
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

		String[] finalMessage = message;
		Optional.ofNullable(embed.getTitle()).ifPresent(title -> embedBuilder.setTitle(finalMessage[0]));
		Optional.ofNullable(embed.getDescription()).ifPresent(description -> embedBuilder.setDescription(finalMessage[1]));
		Optional.ofNullable(embed.getColor()).map(componentService::getColorComponent).ifPresent(embedBuilder::setColor);
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
			String fieldName = messageFormatterUtil.formatMessage(user, field.getName());
			String fieldValue = messageFormatterUtil.formatMessage(user, field.getValue());
			if (replacement.isPresent()) {
				fieldName = messageFormatterUtil.replacePlaceholders(fieldName, replacement.get());
				fieldValue = messageFormatterUtil.replacePlaceholders(fieldValue, replacement.get());
			}

			embedBuilder.addField(fieldName, fieldValue, field.isInline());
		});

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
