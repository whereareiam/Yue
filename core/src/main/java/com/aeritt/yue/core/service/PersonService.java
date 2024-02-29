package com.aeritt.yue.core.service;

import jakarta.persistence.EntityNotFoundException;
import com.aeritt.yue.core.database.entity.Language;
import com.aeritt.yue.core.database.entity.Person;
import com.aeritt.yue.core.database.entity.PersonAdditionalLanguage;
import com.aeritt.yue.core.database.entity.PersonRole;
import com.aeritt.yue.core.database.repository.LanguageRepository;
import com.aeritt.yue.core.database.repository.PersonAdditionalLanguageRepository;
import com.aeritt.yue.core.database.repository.PersonRepository;
import com.aeritt.yue.core.database.repository.PersonRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService implements com.aeritt.yue.api.service.PersonService {
	private final PersonRepository repository;
	private final LanguageRepository languageRepository;
	private final PersonAdditionalLanguageRepository additionalLanguageRepository;
	private final PersonRoleRepository roleRepository;

	@Autowired
	public PersonService(@Lazy LanguageRepository languageRepository, @Lazy PersonRepository repository,
	                     @Lazy PersonAdditionalLanguageRepository additionalLanguageRepository,
	                     @Lazy PersonRoleRepository roleRepository) {
		this.languageRepository = languageRepository;
		this.repository = repository;
		this.additionalLanguageRepository = additionalLanguageRepository;
		this.roleRepository = roleRepository;
	}

	@Caching(evict = {
			@CacheEvict(value = "userLanguage", key = "#userId"),
			@CacheEvict(value = "users", allEntries = true)
	})
	public void addUser(String userId, String globalName, String name, String langCode) {
		Person person = new Person();
		person.setId(userId);
		person.setGlobalName(globalName);
		person.setName(name);

		Language language = languageRepository.findByCode(langCode).orElseThrow();
		person.setMainLanguage(language);

		repository.save(person);
	}

	@Caching(evict = {
			@CacheEvict(value = "userLanguage", key = "#userId"),
			@CacheEvict(value = "users", allEntries = true)
	})
	public void deleteUser(String userId) {
		Person person = repository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("Person not found with id " + userId));

		List<PersonAdditionalLanguage> additionalLanguages = additionalLanguageRepository.findByPersonId(person.getId());
		additionalLanguageRepository.deleteAll(additionalLanguages);

		List<PersonRole> roles = roleRepository.findByPerson(person);
		roleRepository.deleteAll(roles);

		repository.delete(person);
	}

	@Cacheable(value = "users")
	public long getUserCount() {
		return repository.count();
	}
}
