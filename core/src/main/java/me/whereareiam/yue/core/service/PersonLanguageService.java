package me.whereareiam.yue.core.service;

import me.whereareiam.yue.core.database.entity.Language;
import me.whereareiam.yue.core.database.entity.Person;
import me.whereareiam.yue.core.database.entity.PersonAdditionalLanguage;
import me.whereareiam.yue.core.database.repository.LanguageRepository;
import me.whereareiam.yue.core.database.repository.PersonAdditionalLanguageRepository;
import me.whereareiam.yue.core.database.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonLanguageService {
	private final PersonRepository personRepository;
	private final LanguageRepository languageRepository;
	private final PersonAdditionalLanguageRepository personAdditionalLanguageRepository;

	@Autowired
	public PersonLanguageService(PersonRepository personRepository, LanguageRepository languageRepository,
	                             PersonAdditionalLanguageRepository personAdditionalLanguageRepository) {
		this.personRepository = personRepository;
		this.languageRepository = languageRepository;
		this.personAdditionalLanguageRepository = personAdditionalLanguageRepository;
	}

	public void addAdditionalLanguage(String userId, String langCode) {
		Person person = personRepository.findById(userId).orElseThrow();
		Language language = languageRepository.findByCode(langCode).orElseThrow();

		PersonAdditionalLanguage personAdditionalLanguage = new PersonAdditionalLanguage();
		personAdditionalLanguage.setPersonId(person.getId());
		personAdditionalLanguage.setLanguageId(language.getId());
		personAdditionalLanguageRepository.save(personAdditionalLanguage);
	}

	public void removeAdditionalLanguage(String userId, String langCode) {
		Language language = languageRepository.findByCode(langCode).orElseThrow();
		personAdditionalLanguageRepository.deleteByPersonIdAndLanguageId(userId, language.getId());
	}

	@Cacheable(value = "userLanguage", key = "#userId")
	public Optional<String> getUserLanguage(String userId) {
		Optional<Person> user = personRepository.findById(userId);
		return user.map(person -> person.getMainLanguage().getCode());
	}
}
