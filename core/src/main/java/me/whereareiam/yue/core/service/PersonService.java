package me.whereareiam.yue.core.service;

import jakarta.persistence.EntityNotFoundException;
import me.whereareiam.yue.core.database.entity.Language;
import me.whereareiam.yue.core.database.entity.Person;
import me.whereareiam.yue.core.database.entity.PersonAdditionalLanguage;
import me.whereareiam.yue.core.database.entity.PersonRole;
import me.whereareiam.yue.core.database.repository.LanguageRepository;
import me.whereareiam.yue.core.database.repository.PersonAdditionalLanguageRepository;
import me.whereareiam.yue.core.database.repository.PersonRepository;
import me.whereareiam.yue.core.database.repository.PersonRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService implements me.whereareiam.yue.api.service.PersonService {
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

	@CacheEvict(value = "userLanguage", key = "#userId")
	public void addUser(String userId, String globalName, String name, String langCode) {
		Person person = new Person();
		person.setId(userId);
		person.setGlobalName(globalName);
		person.setName(name);

		Language language = languageRepository.findByCode(langCode).orElseThrow();
		person.setMainLanguage(language);

		repository.save(person);
	}

	@CacheEvict(value = "userLanguage", key = "#userId")
	public void deleteUser(String userId) {
		Person person = repository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("Person not found with id " + userId));

		List<PersonAdditionalLanguage> additionalLanguages = additionalLanguageRepository.findByPersonId(person.getId());
		additionalLanguageRepository.deleteAll(additionalLanguages);

		List<PersonRole> roles = roleRepository.findByPerson(person);
		roleRepository.deleteAll(roles);

		repository.delete(person);
	}


	public long getUserCount() {
		return repository.count();
	}
}
