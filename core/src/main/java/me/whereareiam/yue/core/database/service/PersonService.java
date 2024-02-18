package me.whereareiam.yue.core.database.service;

import me.whereareiam.yue.core.database.entity.*;
import me.whereareiam.yue.core.database.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
	private final LanguageRepository languageRepository;
	private final PersonRepository personRepository;
	private final PersonAdditionalLanguageRepository personAdditionalLanguageRepository;
	private final PersonRoleRepository personRoleRepository;
	private final RoleRepository roleRepository;

	@Autowired
	public PersonService(@Lazy LanguageRepository languageRepository, @Lazy PersonRepository personRepository,
	                     @Lazy PersonAdditionalLanguageRepository personAdditionalLanguageRepository,
	                     @Lazy PersonRoleRepository personRoleRepository, @Lazy RoleRepository roleRepository) {
		this.languageRepository = languageRepository;
		this.personRepository = personRepository;
		this.personAdditionalLanguageRepository = personAdditionalLanguageRepository;
		this.personRoleRepository = personRoleRepository;
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

		personRepository.save(person);
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

	@CacheEvict(value = "userLanguage", key = "#userId")
	public void deleteUser(String userId) {
		personRepository.deleteById(userId);
	}

	@Cacheable(value = "userLanguage", key = "#userId")
	public Optional<String> getUserLanguage(String userId) {
		Optional<Person> user = personRepository.findById(userId);
		return user.map(person -> person.getMainLanguage().getCode());
	}

	public List<PersonRole> getPersonsByRole(String roleId) {
		Optional<Role> role = roleRepository.findById(roleId);
		if (role.isPresent()) {
			return personRoleRepository.findByRole(role.get());
		} else {
			return new ArrayList<>();
		}
	}

	public long getUserCount() {
		return personRepository.count();
	}
}
