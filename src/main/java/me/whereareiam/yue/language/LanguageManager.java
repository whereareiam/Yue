package me.whereareiam.yue.language;

import jakarta.annotation.PostConstruct;
import me.whereareiam.yue.config.setting.SettingsConfig;
import me.whereareiam.yue.model.Language;
import me.whereareiam.yue.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class LanguageManager {
	private final SettingsConfig settingsConfig;
	private final Path langPath;

	private Map<Language, Map<String, String>> translations = new HashMap<>();

	@Autowired
	public LanguageManager(SettingsConfig settingsConfig, @Qualifier("languagePath") Path langPath) {
		this.settingsConfig = settingsConfig;
		this.langPath = langPath;
	}

	@PostConstruct
	public void onLoad() {
		File[] moduleDirs = langPath.toFile().listFiles(File::isDirectory);
	}

	public String getTranslation(String key) {
		Language language = settingsConfig.getDefaultLanguage();
		Map<String, String> languageTranslations = translations.get(language);
		if (languageTranslations != null) {
			return languageTranslations.getOrDefault(key, key);
		} else {
			return key;
		}
	}

	public String getTranslation(Person person, String key) {
		Language language;
		if (person.getLanguage() == null) {
			language = settingsConfig.getDefaultLanguage();
		} else {
			language = person.getLanguage().get(0);
		}
		Map<String, String> languageTranslations = translations.get(language);
		if (languageTranslations != null) {
			return languageTranslations.getOrDefault(key, key);
		} else {
			return key;
		}
	}
}
