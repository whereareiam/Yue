package com.aeritt.yue.api.service;

import net.dv8tion.jda.api.entities.User;

public interface LanguageProvider {
	String getTranslation(User user, String key);

	String getTranslation(String key);

	int getTranslationCount();
}
