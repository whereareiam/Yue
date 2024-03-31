package com.aeritt.yue.language;

import com.aeritt.yue.api.config.SettingsConfig;
import com.aeritt.yue.api.model.Language;
import com.aeritt.yue.api.service.UserService;
import lombok.Getter;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class LanguageService implements com.aeritt.yue.api.service.LanguageService {
	private final SettingsConfig settingsConfig;
	private final UserService userService;
	@Getter
	private final Map<String, Map<String, String>> translations = new HashMap<>();

	@Autowired
	public LanguageService(SettingsConfig settingsConfig, UserService userService) {
		this.settingsConfig = settingsConfig;
		this.userService = userService;
	}

	public void registerTranslation(String key, String langCode, String value) {
		translations.computeIfAbsent(langCode, k -> new HashMap<>()).put(key, value);
	}

	public String getTranslation(User user, String key) {
		Language language = userService.getUserLanguage(user.getId());
		if (language == null) {
			language = getDefaultLanguage();
		}

		Map<String, String> langTranslations = translations.get(language.getCode());
		String translation = langTranslations != null ? langTranslations.get(key) : null;
		return translation != null ? translation : key;
	}

	public String getTranslation(String key) {
		Language language = getDefaultLanguage();

		Map<String, String> langTranslations = translations.get(language.getCode());
		String translation = langTranslations != null ? langTranslations.get(key) : null;
		return translation != null ? translation : key;
	}

	public Language getDefaultLanguage() {
		return new Language(Locale.forLanguageTag(
				settingsConfig.getDefaultLanguage()).getDisplayName(),
				settingsConfig.getDefaultLanguage()
		);
	}

	public int getLanguageCount() {
		return translations.values().stream().mapToInt(Map::size).sum();
	}

	public Language getLanguage(String langCode) {
		// TODO Database request
		return null;
	}

	public void registerLanguage(Language language) {
		// TODO Database request
	}
}
