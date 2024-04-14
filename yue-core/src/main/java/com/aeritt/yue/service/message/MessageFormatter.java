package com.aeritt.yue.service.message;

import com.aeritt.yue.api.model.language.Placeholder;
import com.aeritt.yue.language.LanguageProvider;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageFormatter implements com.aeritt.yue.api.message.MessageFormatter {
	private final LanguageProvider languageProvider;

	@Autowired
	public MessageFormatter(LanguageProvider languageProvider) {
		this.languageProvider = languageProvider;
	}

	@Override
	public String[] formatMessage(User user, String[] message, List<Placeholder> placeholders) {
		for (int i = 0; i < message.length; i++) {
			message[i] = formatMessage(user, message[i], placeholders);
		}

		return message;
	}

	@Override
	public String[] formatMessage(User user, String[] message) {
		for (int i = 0; i < message.length; i++) {
			message[i] = formatMessage(user, message[i]);
		}

		return message;
	}

	@Override
	public String formatMessage(User user, String message) {
		message = languageProvider.getTranslation(user, message);
		message = message.contains("$t{") ? hookTranslationPlaceholders(message) : message;
		message = hookEmojiParser(message);

		return message;
	}

	@Override
	public String formatMessage(String message) {
		message = languageProvider.getTranslation(message);
		message = message.contains("$t{") ? hookTranslationPlaceholders(message) : message;
		message = hookEmojiParser(message);

		return message;
	}

	@Override
	public String formatMessage(User user, String message, List<Placeholder> placeholders) {
		message = languageProvider.getTranslation(user, message, placeholders);
		message = message.contains("$t{") ? hookTranslationPlaceholders(message) : message;
		message = hookEmojiParser(message);

		return message;
	}

	private String hookEmojiParser(String message) {
		if (message.startsWith(":") && message.endsWith(":")) {
			return EmojiParser.parseToUnicode(message);
		}

		return message;
	}

	private String hookTranslationPlaceholders(String message) {
		if (message == null) return null;

		final Pattern pattern = Pattern.compile("\\$t\\{(.+?)\\}");

		Matcher matcher = pattern.matcher(message);
		StringBuilder buffer = new StringBuilder();
		while (matcher.find()) {
			String key = matcher.group(1);
			String translation = languageProvider.getTranslation(key);
			String escapedTranslation = translation.replace("$", "\\$");
			matcher.appendReplacement(buffer, escapedTranslation);
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}
}
