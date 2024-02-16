package me.whereareiam.yue.core.util.message;

import me.whereareiam.yue.core.language.LanguageService;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageFormatterUtil {
	private final LanguageService languageService;

	@Autowired
	public MessageFormatterUtil(LanguageService languageService) {
		this.languageService = languageService;
	}

	public String[] formatMessage(String[] message, Member member) {
		for (int i = 0; i < message.length; i++) {
			message[i] = formatMessage(message[i], member);
		}

		return message;
	}

	public String formatMessage(String message, Member member) {
		message = languageService.getTranslation(message);
		message = hookTranslationPlaceholders(message);
		message = hookInternalPlaceholders(message, member);

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
			matcher.appendReplacement(buffer, translation);
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}

	private String hookInternalPlaceholders(String message, Member member) {
		return message.replace("{memberTag}", member.getAsMention());
	}
}
