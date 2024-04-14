package com.aeritt.yue.api.service;

import com.aeritt.yue.api.model.language.Placeholder;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public interface LanguageProvider {
	String getTranslation(User user, String key, List<Placeholder> placeholders);

	String getTranslation(User user, String key);

	String getTranslation(String key);

	int getTranslationCount();
}
