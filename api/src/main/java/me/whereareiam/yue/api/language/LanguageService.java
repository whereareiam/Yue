package me.whereareiam.yue.api.language;

import net.dv8tion.jda.api.entities.User;

public interface LanguageService {
	void registerTranslation(String key, String langCode, String value);

	String getTranslation(User user, String key);

	String getTranslation(String key);
}
