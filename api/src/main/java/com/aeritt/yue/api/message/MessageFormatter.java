package com.aeritt.yue.api.message;

import net.dv8tion.jda.api.entities.User;

public interface MessageFormatter {
	String[] formatMessage(User user, String[] message);

	String formatMessage(String message);

	String formatMessage(User user, String message);

	String[] replacePlaceholders(String[] message, PlaceholderReplacement replacement);

	String replacePlaceholders(String message, PlaceholderReplacement replacement);
}
