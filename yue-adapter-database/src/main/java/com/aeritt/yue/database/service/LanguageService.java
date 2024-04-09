package com.aeritt.yue.database.service;

import com.aeritt.yue.api.config.SettingsConfig;
import com.aeritt.yue.api.model.Language;
import com.aeritt.yue.database.mapper.LanguageMapper;
import com.aeritt.yue.database.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class LanguageService implements com.aeritt.yue.api.service.LanguageService {
	private final SettingsConfig settingsConfig;

	// Repositories
	private final LanguageRepository languageRepository;

	// Mapper
	private final LanguageMapper languageMapper;

	@Autowired
	public LanguageService(SettingsConfig settingsConfig, LanguageRepository languageRepository,
	                       LanguageMapper languageMapper) {
		this.settingsConfig = settingsConfig;
		this.languageRepository = languageRepository;
		this.languageMapper = languageMapper;
	}

	@Override
	public boolean registerLanguage(Language language) {
		com.aeritt.yue.database.entity.Language languageEntity = languageMapper.entityToModel(language);

		if (languageRepository.existsByCode(language.getCode())) return false;

		languageRepository.save(languageEntity);
		return true;
	}

	@Override
	public boolean unregisterLanguage(Language language) {
		if (!languageRepository.existsByCode(language.getCode())) return false;

		languageRepository.deleteByCode(language.getCode());
		return true;
	}

	@Override
	public Optional<Language> getLanguage(String langCode) {
		return languageRepository.findByCode(langCode)
				.map(languageMapper::modelToEntity);
	}

	@Override
	public Language getDefaultLanguage() {
		return new Language(Locale.forLanguageTag(
				settingsConfig.getDefaultLanguage()).getDisplayName(),
				settingsConfig.getDefaultLanguage()
		);
	}

	@Override
	public List<Language> getLanguages() {
		return languageRepository.findAll().stream()
				.map(languageMapper::modelToEntity)
				.toList();
	}
}
