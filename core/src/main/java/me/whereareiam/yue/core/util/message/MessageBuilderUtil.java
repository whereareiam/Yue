package me.whereareiam.yue.core.util.message;

import me.whereareiam.yue.core.config.component.ButtonsConfig;
import me.whereareiam.yue.core.config.component.palette.PaletteConfig;
import me.whereareiam.yue.core.model.CustomButton;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class MessageBuilderUtil {
	private final PaletteConfig paletteConfig;
	private final ButtonsConfig buttonsConfig;
	private final MessageFormatterUtil messageFormatterUtil;

	@Autowired
	public MessageBuilderUtil(PaletteConfig paletteConfig, ButtonsConfig buttonsConfig,
	                          MessageFormatterUtil messageFormatterUtil) {
		this.paletteConfig = paletteConfig;
		this.buttonsConfig = buttonsConfig;
		this.messageFormatterUtil = messageFormatterUtil;
	}

	public MessageEmbed primaryEmbed(User user, String title, String description, String footer) {
		return embed(user, title, description, footer, Color.decode(paletteConfig.getMain().getPrimary()));
	}

	public MessageEmbed dangerEmbed(User user, String title, String description, String footer) {
		return embed(user, title, description, footer, Color.decode(paletteConfig.getMain().getDanger()));
	}

	public MessageEmbed embed(User user, String title, String description, String footer, Color color) {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		String[] message = {title, description, footer};
		message = messageFormatterUtil.formatMessage(user, message);

		embedBuilder.setTitle(message[0]);
		embedBuilder.setDescription(message[1]);
		embedBuilder.setFooter(message[2]);
		embedBuilder.setColor(color);

		return embedBuilder.build();
	}

	public Button button(User user, String buttonId) {
		CustomButton customButton = buttonsConfig.getButtons().stream()
				.filter(button -> button.getId().equals(buttonId))
				.findFirst()
				.orElseThrow();

		String label = customButton.getLabel();
		label = messageFormatterUtil.formatMessage(user, label);

		return Button.of(customButton.getStyle(), customButton.getId(), label);
	}
}
