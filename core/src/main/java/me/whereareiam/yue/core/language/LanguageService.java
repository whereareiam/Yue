package me.whereareiam.yue.core.language;

import me.whereareiam.yue.core.config.setting.SettingsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LanguageService {
	private final SettingsConfig settingsConfig;
	private final Map<String, Map<String, String>> translations = new HashMap<>();

	@Autowired
	public LanguageService(SettingsConfig settingsConfig) {
		this.settingsConfig = settingsConfig;
	}

	public void registerTranslation(String key, String langCode, String value) {
		translations.computeIfAbsent(langCode, k -> new HashMap<>()).put(key, value);
	}

	public String[] getTranslation(String[] keys) {
		for (int i = 0; i < keys.length; i++) {
			keys[i] = getTranslation(keys[i]);
		}

		return keys;
	}

	public String getTranslation(String key) {
		String langCode = settingsConfig.getDefaultLanguage();

		Map<String, String> langTranslations = translations.get(langCode);
		String translation = langTranslations != null ? langTranslations.get(key) : null;
		return translation != null ? translation : key;
	}
}
