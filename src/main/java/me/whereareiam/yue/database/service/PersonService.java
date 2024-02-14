package me.whereareiam.yue.database.service;

import me.whereareiam.yue.database.entity.Person;
import me.whereareiam.yue.database.entity.PersonLanguage;
import me.whereareiam.yue.database.repository.PersonLanguageRepository;
import me.whereareiam.yue.database.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {
	private final PersonRepository personRepository;
	private final PersonLanguageRepository personLanguageRepository;

	@Autowired
	public PersonService(@Lazy PersonRepository personRepository, @Lazy PersonLanguageRepository personLanguageRepository) {
		this.personRepository = personRepository;
		this.personLanguageRepository = personLanguageRepository;
	}

	@Cacheable(value = "userLanguage", key = "#userId")
	public Optional<String> getUserLanguage(int userId) {
		Optional<Person> user = personRepository.findById(userId);
		if (user.isPresent()) {
			Optional<PersonLanguage> userLanguage = personLanguageRepository.findById(user.get().getId());
			if (userLanguage.isPresent()) {
				return Optional.of(userLanguage.get().getLanguage().getCode());
			}
		}
		return Optional.empty();
	}
}
