package me.whereareiam.yue.core.service;

import me.whereareiam.yue.core.database.entity.Language;
import me.whereareiam.yue.core.database.entity.Person;
import me.whereareiam.yue.core.database.entity.PersonAdditionalLanguage;
import me.whereareiam.yue.core.database.repository.LanguageRepository;
import me.whereareiam.yue.core.database.repository.PersonAdditionalLanguageRepository;
import me.whereareiam.yue.core.database.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonLanguageService implements me.whereareiam.yue.api.service.PersonLanguageService {
	private final PersonRepository personRepository;
	private final LanguageRepository languageRepository;
	private final PersonAdditionalLanguageRepository additionalLanguageRepository;

	@Autowired
	public PersonLanguageService(PersonRepository personRepository, LanguageRepository languageRepository,
	                             PersonAdditionalLanguageRepository additionalLanguageRepository) {
		this.personRepository = personRepository;
		this.languageRepository = languageRepository;
		this.additionalLanguageRepository = additionalLanguageRepository;
	}

	@CacheEvict(value = "userLanguage", key = "#userId")
	public void setLanguage(String userId, String langCode) {
		Person person = personRepository.findById(userId).orElseThrow();
		Language language = languageRepository.findByCode(langCode).orElseThrow();
		person.setMainLanguage(language);

		personRepository.save(person);
	}

	public void addAdditionalLanguage(String userId, String langCode) {
		Person person = personRepository.findById(userId).orElseThrow();
		Language language = languageRepository.findByCode(langCode).orElseThrow();

		PersonAdditionalLanguage personAdditionalLanguage = new PersonAdditionalLanguage();
		personAdditionalLanguage.setPersonId(person.getId());
		personAdditionalLanguage.setLanguageId(language.getId());

		additionalLanguageRepository.save(personAdditionalLanguage);
	}

	public void removeAdditionalLanguage(String userId, String langCode) {
		Language language = languageRepository.findByCode(langCode).orElseThrow();
		additionalLanguageRepository.deleteByPersonIdAndLanguageId(userId, language.getId());
	}

	@Cacheable(value = "userLanguage", key = "#userId")
	public Optional<String> getUserLanguage(String userId) {
		Optional<Person> user = personRepository.findById(userId);
		return user.map(person -> person.getMainLanguage().getCode());
	}
}
