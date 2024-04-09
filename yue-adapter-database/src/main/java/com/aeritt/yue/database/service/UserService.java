package com.aeritt.yue.database.service;

import com.aeritt.yue.api.event.EventManager;
import com.aeritt.yue.api.event.internal.language.LanguageChangeEvent;
import com.aeritt.yue.api.event.internal.user.UserCreatedEvent;
import com.aeritt.yue.api.event.internal.user.UserDeletedEvent;
import com.aeritt.yue.api.model.Language;
import com.aeritt.yue.api.model.UserProfile;
import com.aeritt.yue.database.entity.Role;
import com.aeritt.yue.database.entity.user.User;
import com.aeritt.yue.database.entity.user.UserAdditionalLanguage;
import com.aeritt.yue.database.entity.user.UserRole;
import com.aeritt.yue.database.mapper.LanguageMapper;
import com.aeritt.yue.database.mapper.UserMapper;
import com.aeritt.yue.database.repository.LanguageRepository;
import com.aeritt.yue.database.repository.RoleRepository;
import com.aeritt.yue.database.repository.user.UserAdditionalLanguageRepository;
import com.aeritt.yue.database.repository.user.UserRepository;
import com.aeritt.yue.database.repository.user.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements com.aeritt.yue.api.service.UserService {
	// Repositories
	private final UserRepository userRepository;
	private final LanguageRepository languageRepository;
	private final UserRoleRepository userRoleRepository;
	private final UserAdditionalLanguageRepository additionalLanguageRepository;
	private final RoleRepository roleRepository;

	// Mappers
	private final UserMapper userMapper;
	private final LanguageMapper languageMapper;

	// Event
	private final EventManager eventManager;

	@Autowired
	public UserService(UserRepository userRepository, LanguageRepository languageRepository,
	                   UserRoleRepository userRoleRepository, UserAdditionalLanguageRepository additionalLanguageRepository,
	                   RoleRepository roleRepository, UserMapper userMapper, LanguageMapper languageMapper,
	                   EventManager eventManager) {
		this.userRepository = userRepository;
		this.languageRepository = languageRepository;
		this.userRoleRepository = userRoleRepository;
		this.additionalLanguageRepository = additionalLanguageRepository;
		this.roleRepository = roleRepository;
		this.userMapper = userMapper;
		this.languageMapper = languageMapper;
		this.eventManager = eventManager;
	}

	@Override
	@Caching(evict = {
			@CacheEvict(value = "userLanguage", key = "#userId"),
			@CacheEvict(value = "users", allEntries = true)
	})
	public boolean addUser(String userId, String globalName, String name, String langCode) {
		User user = new User();
		user.setId(userId);
		user.setGlobalName(globalName);
		user.setName(name);

		com.aeritt.yue.database.entity.Language language = languageRepository.findByCode(langCode).orElse(null);
		if (language == null) throw new EntityNotFoundException("Language not found");

		user.setMainLanguage(language);

		UserCreatedEvent event = new UserCreatedEvent(userMapper.map(user));
		eventManager.fireEvent(event);
		if (event.isCancelled()) return false;

		userRepository.save(user);
		return true;
	}

	@Override
	@Caching(evict = {
			@CacheEvict(value = "userLanguage", key = "#userId"),
			@CacheEvict(value = "users", allEntries = true)
	})
	public boolean deleteUser(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));

		List<UserAdditionalLanguage> additionalLanguages = additionalLanguageRepository.findByUserId(user.getId());
		List<UserRole> roles = userRoleRepository.findByUser(user);

		UserDeletedEvent event = new UserDeletedEvent(userMapper.map(user));
		eventManager.fireEvent(event);
		if (event.isCancelled()) return false;

		userRoleRepository.deleteAll(roles);
		userRepository.delete(user);
		additionalLanguageRepository.deleteAll(additionalLanguages);
		return true;
	}

	@Override
	public boolean userExists(String userId) {
		return userRepository.existsById(userId);
	}

	@Override
	public Optional<UserProfile> getUserById(String id) {
		Optional<User> user = userRepository.findById(id);
		return user.map(userMapper::map);
	}

	@Override
	public long getUserCount() {
		return userRepository.count();
	}

	@Override
	public boolean setLanguage(String userId, String langCode) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) return false;

		Optional<com.aeritt.yue.database.entity.Language> language = languageRepository.findByCode(langCode);
		if (language.isEmpty()) return false;

		LanguageChangeEvent event = new LanguageChangeEvent(userMapper.map(user.get()),
				Optional.of(languageMapper.modelToEntity(user.get().getMainLanguage())));
		eventManager.fireEvent(event);
		if (event.isCancelled()) return false;

		user.get().setMainLanguage(language.get());
		userRepository.save(user.get());
		return true;
	}

	@Override
	public boolean addAdditionalLanguage(String userId, String langCode) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) return false;

		Optional<com.aeritt.yue.database.entity.Language> language = languageRepository.findByCode(langCode);
		if (language.isEmpty()) return false;

		// TODO Event

		UserAdditionalLanguage additionalLanguage = new UserAdditionalLanguage();
		additionalLanguage.setUser(user.get());
		additionalLanguage.setLanguage(language.get());
		additionalLanguageRepository.save(additionalLanguage);
		return true;
	}

	@Override
	public boolean removeAdditionalLanguage(String userId, String langCode) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) return false;

		Optional<com.aeritt.yue.database.entity.Language> language = languageRepository.findByCode(langCode);
		if (language.isEmpty()) return false;

		List<UserAdditionalLanguage> additionalLanguages = additionalLanguageRepository.findByUserId(user.get().getId());
		if (additionalLanguages.isEmpty()) return false;

		// TODO Event

		additionalLanguageRepository.deleteByUserAndLanguage(user.get(), language.get());
		return true;
	}

	@Override
	public Language getUserLanguage(String userId) {
		Optional<User> user = userRepository.findById(userId);
		return user.map(value -> languageMapper.modelToEntity(value.getMainLanguage())).orElse(null);

	}

	@Override
	public boolean addRole(String userId, String roleId) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) return false;

		Optional<Role> role = roleRepository.findById(roleId);
		if (role.isEmpty()) return false;

		UserRole userRole = new UserRole();
		userRole.setUser(user.get());
		userRole.setRole(role.get());

		// TODO Event

		userRoleRepository.save(userRole);
		return true;
	}

	@Override
	public boolean removeRole(String userId, String roleId) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) return false;

		Optional<Role> role = roleRepository.findById(roleId);
		if (role.isEmpty()) return false;

		List<UserRole> roles = userRoleRepository.findByUser(user.get());
		if (roles.isEmpty()) return false;

		// TODO Event

		userRoleRepository.deleteByUserAndRole(user.get(), role.get());
		return true;
	}

	@Override
	public List<String> getUsersByRole(String roleId) {
		Optional<Role> role = roleRepository.findById(roleId);
		if (role.isEmpty()) return List.of();

		List<UserRole> userRoles = userRoleRepository.findByRole(role.get());
		return userRoles.stream().map(userRole -> userRole.getUser().getId()).toList();
	}

	@Override
	public boolean hasRole(String userId, String roleId) {
		return userRoleRepository.existsByUserIdAndRoleId(userId, roleId);
	}
}
