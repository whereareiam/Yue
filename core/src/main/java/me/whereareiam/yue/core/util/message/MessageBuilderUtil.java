package me.whereareiam.yue.core.util.message;

import me.whereareiam.yue.core.config.palette.PaletteConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class MessageBuilderUtil {
	private final PaletteConfig paletteConfig;
	private final MessageFormatterUtil messageFormatterUtil;

	@Autowired
	public MessageBuilderUtil(PaletteConfig paletteConfig, MessageFormatterUtil messageFormatterUtil) {
		this.paletteConfig = paletteConfig;
		this.messageFormatterUtil = messageFormatterUtil;
	}

	public MessageEmbed buildEmbedMessage(Member member, String title, String description, String footer) {
		return buildEmbedMessage(member, title, description, footer, Color.decode(paletteConfig.getMain().getPrimary()));
	}

	public MessageEmbed buildEmbedMessage(Member member, String title, String description, String footer, Color color) {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		String[] message = {title, description, footer};
		message = messageFormatterUtil.formatMessage(message, member);

		embedBuilder.setTitle(message[0]);
		embedBuilder.setDescription(message[1]);
		embedBuilder.setFooter(message[2]);
		embedBuilder.setColor(color);

		return embedBuilder.build();
	}
}
