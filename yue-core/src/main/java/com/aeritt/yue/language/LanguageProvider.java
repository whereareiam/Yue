package com.aeritt.yue.language;

import com.aeritt.yue.api.model.Language;
import com.aeritt.yue.api.model.language.Placeholder;
import com.aeritt.yue.api.model.language.Translation;
import com.aeritt.yue.api.service.LanguageService;
import com.aeritt.yue.api.service.UserService;
import lombok.Getter;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LanguageProvider implements com.aeritt.yue.api.service.LanguageProvider {
	// Services
	private final UserService userService;
	private final LanguageService languageService;

	// Translations
	@Getter
	private final Map<String, Translation> translations = new HashMap<>();

	@Autowired
	public LanguageProvider(UserService userService, LanguageService languageService) {
		this.userService = userService;
		this.languageService = languageService;
	}

	@Override
	public String getTranslation(User user, String key, List<Placeholder> placeholders) {
		Language language = (user != null) ? userService.getUserLanguage(user.getId()) : null;
		if (language == null) {
			language = languageService.getDefaultLanguage();
		}

		Translation translation = translations.get(language.getCode() + key);
		return translation != null ? translation.process(placeholders) : key;
	}

	@Override
	public String getTranslation(User user, String key) {
		return getTranslation(user, key, List.of());
	}

	@Override
	public String getTranslation(String key) {
		return getTranslation(null, key, List.of());
	}

	@Override
	public int getTranslationCount() {
		return translations.size();
	}

	public void registerTranslation(String key, String langCode, String message) {
		translations.putIfAbsent(langCode + key, new Translation(message));
	}
}
