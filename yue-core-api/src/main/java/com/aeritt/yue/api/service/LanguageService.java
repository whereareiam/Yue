package com.aeritt.yue.api.service;

import com.aeritt.yue.api.model.Language;

import java.util.List;
import java.util.Optional;

public interface LanguageService {
	boolean registerLanguage(Language language);

	boolean unregisterLanguage(Language language);

	Optional<Language> getLanguage(String langCode);

	Language getDefaultLanguage();

	List<Language> getLanguages();
}
