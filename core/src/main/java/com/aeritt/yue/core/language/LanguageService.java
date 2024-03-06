package com.aeritt.yue.core.language;

import com.aeritt.yue.core.config.setting.SettingsConfig;
import com.aeritt.yue.core.service.PersonLanguageService;
import lombok.Getter;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LanguageService implements com.aeritt.yue.api.language.LanguageService {
	private final SettingsConfig settingsConfig;
	private final PersonLanguageService personLanguageService;
	@Getter
	private final Map<String, Map<String, String>> translations = new HashMap<>();

	@Autowired
	public LanguageService(SettingsConfig settingsConfig, PersonLanguageService personLanguageService) {
		this.settingsConfig = settingsConfig;
		this.personLanguageService = personLanguageService;
	}

	public void registerTranslation(String key, String langCode, String value) {
		translations.computeIfAbsent(langCode, k -> new HashMap<>()).put(key, value);
	}

	public String getTranslation(User user, String key) {
		Optional<String> langCode = personLanguageService.getUserLanguage(user.getId());
		if (langCode.isEmpty()) {
			langCode = Optional.of(getDefaultLanguage());
		}

		Map<String, String> langTranslations = translations.get(langCode.get());
		String translation = langTranslations != null ? langTranslations.get(key) : null;
		return translation != null ? translation : key;
	}

	public String getTranslation(String key) {
		String langCode = getDefaultLanguage();

		Map<String, String> langTranslations = translations.get(langCode);
		String translation = langTranslations != null ? langTranslations.get(key) : null;
		return translation != null ? translation : key;
	}

	public String getDefaultLanguage() {
		return settingsConfig.getDefaultLanguage();
	}

	public int getLanguageCount() {
		return translations.values().stream().mapToInt(Map::size).sum();
	}
}
