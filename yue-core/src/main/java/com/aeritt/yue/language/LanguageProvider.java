package com.aeritt.yue.language;

import com.aeritt.yue.api.model.Language;
import com.aeritt.yue.api.service.LanguageService;
import com.aeritt.yue.api.service.UserService;
import lombok.Getter;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LanguageProvider implements com.aeritt.yue.api.service.LanguageProvider {
	/*
	 TODO 1: Add selectors for the translation that will work like if else statements,
	  in the end they will just return some string
	 TODO 2: Allow selectors to be nested
	 TODO 3: Allow selectors to return values based on some translation placeholder.
	 */

	// Services
	private final UserService userService;
	private final LanguageService languageService;

	// Translations
	@Getter
	private final Map<String, Map<String, String>> translations = new HashMap<>();

	@Autowired
	public LanguageProvider(UserService userService, LanguageService languageService) {
		this.userService = userService;
		this.languageService = languageService;
	}

	@Override
	public String getTranslation(User user, String key) {
		Language language = userService.getUserLanguage(user.getId());
		if (language == null) {
			language = languageService.getDefaultLanguage();
		}

		Map<String, String> langTranslations = translations.get(language.getCode());
		String translation = langTranslations != null ? langTranslations.get(key) : null;
		return translation != null ? translation : key;
	}

	@Override
	public String getTranslation(String key) {
		Language language = languageService.getDefaultLanguage();

		Map<String, String> langTranslations = translations.get(language.getCode());
		String translation = langTranslations != null ? langTranslations.get(key) : null;
		return translation != null ? translation : key;
	}

	@Override
	public int getTranslationCount() {
		return translations.values().stream().mapToInt(Map::size).sum();
	}

	public void registerTranslation(String key, String langCode, String value) {
		translations.computeIfAbsent(langCode, k -> new HashMap<>()).put(key, value);
	}
}
