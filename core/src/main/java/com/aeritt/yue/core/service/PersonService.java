package com.aeritt.yue.core.service;

import com.aeritt.yue.api.service.PersonLanguageService;
import com.aeritt.yue.core.database.entity.Language;
import com.aeritt.yue.core.database.entity.person.Person;
import com.aeritt.yue.core.database.entity.person.PersonAdditionalLanguage;
import com.aeritt.yue.core.database.entity.person.PersonRole;
import com.aeritt.yue.core.database.repository.LanguageRepository;
import com.aeritt.yue.core.database.repository.person.PersonAdditionalLanguageRepository;
import com.aeritt.yue.core.database.repository.person.PersonRepository;
import com.aeritt.yue.core.database.repository.person.PersonRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService implements com.aeritt.yue.api.service.PersonService {
	private final PersonRepository repository;
	private final LanguageRepository languageRepository;
	private final PersonAdditionalLanguageRepository additionalLanguageRepository;
	private final PersonRoleRepository roleRepository;
	private final ApplicationContext ctx;

	@Autowired
	public PersonService(@Lazy LanguageRepository languageRepository, @Lazy PersonRepository repository,
	                     @Lazy PersonAdditionalLanguageRepository additionalLanguageRepository,
	                     @Lazy PersonRoleRepository roleRepository, @Qualifier ApplicationContext ctx) {
		this.languageRepository = languageRepository;
		this.repository = repository;
		this.additionalLanguageRepository = additionalLanguageRepository;
		this.roleRepository = roleRepository;
		this.ctx = ctx;
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

	public boolean userExists(String userId) {
		return repository.existsById(userId);
	}

	@Cacheable(value = "users")
	public long getUserCount() {
		return repository.count();
	}

	public PersonLanguageService getPersonLanguageService() {
		return ctx.getBean(PersonLanguageService.class);
	}

	public PersonRoleService getPersonRoleService() {
		return ctx.getBean(PersonRoleService.class);
	}
}
