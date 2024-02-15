package me.whereareiam.yue.core.language;

import me.whereareiam.yue.core.config.setting.SettingsConfig;
import me.whereareiam.yue.core.database.entity.Language;
import me.whereareiam.yue.core.database.repository.LanguageRepository;
import me.whereareiam.yue.core.database.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@DependsOn("databaseManager")
public class LanguageManager {
	private final LanguageRepository languageRepository;
	private final PersonService personService;
	private final SettingsConfig settingsConfig;
	private final Path langPath;

	private Map<String, Map<String, String>> translations = new HashMap<>();

	@Autowired
	public LanguageManager(LanguageRepository languageRepository, PersonService personService, SettingsConfig settingsConfig,
	                       @Qualifier("languagePath") Path langPath) {
		this.languageRepository = languageRepository;
		this.personService = personService;
		this.settingsConfig = settingsConfig;
		this.langPath = langPath;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onLoad() {
		File[] moduleDirs = langPath.toFile().listFiles(File::isDirectory);

	}

	private void addDefaultLanguages() {
		Language english = new Language();
		english.setId(0);
		english.setCode("en");
		english.setName("English");

		Language german = new Language();
		german.setId(1);
		german.setCode("de");
		german.setName("German");

		Language russian = new Language();
		russian.setId(2);
		russian.setCode("ru");
		russian.setName("Russian");

		languageRepository.saveAll(List.of(english, german, russian));
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
