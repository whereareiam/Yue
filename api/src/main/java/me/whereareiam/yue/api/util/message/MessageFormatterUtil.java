package me.whereareiam.yue.api.util.message;

import net.dv8tion.jda.api.entities.User;

public interface MessageFormatterUtil {
	String[] formatMessage(User user, String[] message);

	String formatMessage(String message);

	String formatMessage(User user, String message);

	String[] replacePlaceholders(String[] message, PlaceholderReplacement replacement);

	String replacePlaceholders(String message, PlaceholderReplacement replacement);
}
