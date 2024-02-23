package me.whereareiam.yue.api.service;

import java.util.Optional;

public interface PersonLanguageService {
	void setLanguage(String userId, String langCode);

	void addAdditionalLanguage(String userId, String langCode);

	void removeAdditionalLanguage(String userId, String langCode);

	Optional<String> getUserLanguage(String userId);
}
