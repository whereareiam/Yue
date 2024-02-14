package me.whereareiam.yue.language;

import jakarta.annotation.PostConstruct;
import me.whereareiam.yue.config.setting.SettingsConfig;
import me.whereareiam.yue.database.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LanguageManager {
	private final PersonService personService;
	private final SettingsConfig settingsConfig;
	private final Path langPath;

	private Map<String, Map<String, String>> translations = new HashMap<>();

	@Autowired
	public LanguageManager(PersonService personService, SettingsConfig settingsConfig, @Qualifier("languagePath") Path langPath) {
		this.personService = personService;
		this.settingsConfig = settingsConfig;
		this.langPath = langPath;
	}

	@PostConstruct
	public void onLoad() {
		File[] moduleDirs = langPath.toFile().listFiles(File::isDirectory);
	}

	public String getTranslation(String key) {
		String language = settingsConfig.getDefaultLanguage();
		Map<String, String> languageTranslations = translations.get(language);
		if (languageTranslations != null) {
			return languageTranslations.getOrDefault(key, key);
		} else {
			return key;
		}
	}

	public String getTranslation(int userId, String key) {
		Optional<String> personLanguage = personService.getUserLanguage(userId);
		String language = personLanguage.orElseGet(settingsConfig::getDefaultLanguage);

		Map<String, String> languageTranslations = translations.get(language);
		if (languageTranslations != null) {
			return languageTranslations.getOrDefault(key, key);
		} else {
			return key;
		}
	}
}
