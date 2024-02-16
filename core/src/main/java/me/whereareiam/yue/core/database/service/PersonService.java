package me.whereareiam.yue.core.database.service;

import me.whereareiam.yue.core.database.entity.Person;
import me.whereareiam.yue.core.database.entity.PersonLanguage;
import me.whereareiam.yue.core.database.entity.PersonRole;
import me.whereareiam.yue.core.database.entity.Role;
import me.whereareiam.yue.core.database.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
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
	private final PersonLanguageRepository personLanguageRepository;
	private final PersonRoleRepository personRoleRepository;
	private final RoleRepository roleRepository;

	@Autowired
	public PersonService(@Lazy LanguageRepository languageRepository, @Lazy PersonRepository personRepository,
	                     @Lazy PersonLanguageRepository personLanguageRepository,
	                     @Lazy PersonRoleRepository personRoleRepository, @Lazy RoleRepository roleRepository) {
		this.languageRepository = languageRepository;
		this.personRepository = personRepository;
		this.personLanguageRepository = personLanguageRepository;
		this.personRoleRepository = personRoleRepository;
		this.roleRepository = roleRepository;
	}

	@Cacheable(value = "userLanguage", key = "#userId")
	public Optional<String> getUserLanguage(String userId) {
		Optional<Person> user = personRepository.findById(userId);
		if (user.isPresent()) {
			List<PersonLanguage> userLanguages = personLanguageRepository.findByPersonId(user.get().getId());
			if (!userLanguages.isEmpty()) {
				PersonLanguage userLanguage = userLanguages.get(0);
				return Optional.of(userLanguage.getLanguage().getCode());
			}
		}
		return Optional.empty();
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
