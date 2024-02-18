package me.whereareiam.yue.core.language;

import me.whereareiam.yue.core.config.setting.SettingsConfig;
import me.whereareiam.yue.core.database.service.PersonService;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LanguageService {
	private final SettingsConfig settingsConfig;
	private final PersonService personService;
	private final Map<String, Map<String, String>> translations = new HashMap<>();

	@Autowired
	public LanguageService(SettingsConfig settingsConfig, PersonService personService) {
		this.settingsConfig = settingsConfig;
		this.personService = personService;
	}

	public void registerTranslation(String key, String langCode, String value) {
		translations.computeIfAbsent(langCode, k -> new HashMap<>()).put(key, value);
	}

	public String getTranslation(User user, String key) {
		Optional<String> langCode = personService.getUserLanguage(user.getId());
		if (langCode.isEmpty()) {
			langCode = Optional.of(settingsConfig.getDefaultLanguage());
		}

		Map<String, String> langTranslations = translations.get(langCode.get());
		String translation = langTranslations != null ? langTranslations.get(key) : null;
		return translation != null ? translation : key;
	}

	public String getTranslation(String key) {
		String langCode = settingsConfig.getDefaultLanguage();

		Map<String, String> langTranslations = translations.get(langCode);
		String translation = langTranslations != null ? langTranslations.get(key) : null;
		return translation != null ? translation : key;
	}
}
