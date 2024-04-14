package com.aeritt.yue.api.message;

import com.aeritt.yue.api.model.language.Placeholder;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public interface MessageFormatter {
	String[] formatMessage(User user, String[] message, List<Placeholder> placeholders);

	String[] formatMessage(User user, String... message);

	String formatMessage(User user, String message);

	String formatMessage(String message);

	String formatMessage(User user, String message, List<Placeholder> placeholders);
}
