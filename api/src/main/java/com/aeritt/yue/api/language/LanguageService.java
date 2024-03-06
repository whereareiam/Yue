package com.aeritt.yue.api.language;

import net.dv8tion.jda.api.entities.User;

import java.util.Map;

public interface LanguageService {
	void registerTranslation(String key, String langCode, String value);

	String getTranslation(User user, String key);

	String getTranslation(String key);

	Map<String, Map<String, String>> getTranslations();

	String getDefaultLanguage();

	int getLanguageCount();
}
