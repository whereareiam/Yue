package com.aeritt.yue.core.util.message;

import com.aeritt.yue.api.message.PlaceholderReplacement;
import com.aeritt.yue.core.language.LanguageService;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageFormatter implements com.aeritt.yue.api.message.MessageFormatter {
	private final LanguageService languageService;

	@Autowired
	public MessageFormatter(LanguageService languageService) {
		this.languageService = languageService;
	}

	public String[] formatMessage(User user, String[] message) {
		for (int i = 0; i < message.length; i++) {
			message[i] = formatMessage(user, message[i]);
		}

		return message;
	}

	public String formatMessage(String message) {
		message = languageService.getTranslation(message);
		message = hookTranslationPlaceholders(message);
		message = hookEmojiParser(message);

		return message;
	}

	public String formatMessage(User user, String message) {
		message = languageService.getTranslation(user, message);
		message = hookTranslationPlaceholders(message);
		message = hookInternalPlaceholders(user, message);
		message = hookEmojiParser(message);

		return message;
	}

	private String hookTranslationPlaceholders(String message) {
		if (message == null) return null;

		final Pattern pattern = Pattern.compile("\\$t\\{(.+?)\\}");

		Matcher matcher = pattern.matcher(message);
		StringBuilder buffer = new StringBuilder();
		while (matcher.find()) {
			String key = matcher.group(1);
			String translation = languageService.getTranslation(key);
			String escapedTranslation = translation.replace("$", "\\$");
			matcher.appendReplacement(buffer, escapedTranslation);
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}

	private String hookInternalPlaceholders(User user, String message) {
		return message.replace("{memberTag}", user.getAsMention());
	}

	private String hookEmojiParser(String message) {
		if (message.startsWith(":") && message.endsWith(":")) {
			return EmojiParser.parseToUnicode(message);
		}

		return message;
	}

	public String[] replacePlaceholders(String[] message, PlaceholderReplacement replacement) {
		for (int i = 0; i < message.length; i++) {
			message[i] = replacePlaceholders(message[i], replacement);
		}

		return message;
	}

	public String replacePlaceholders(String message, PlaceholderReplacement replacement) {
		for (int i = 0; i < replacement.getPlaceholders().size(); i++) {
			message = message.replace(replacement.getPlaceholders().get(i), replacement.getReplacements().get(i));
		}

		return message;
	}
}
