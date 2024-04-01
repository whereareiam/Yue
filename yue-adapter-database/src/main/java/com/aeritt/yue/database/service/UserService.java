package com.aeritt.yue.database.service;

import com.aeritt.yue.api.model.Language;
import com.aeritt.yue.api.model.UserProfile;
import com.aeritt.yue.database.entity.user.User;
import com.aeritt.yue.database.mapper.UserMapper;
import com.aeritt.yue.database.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements com.aeritt.yue.api.service.UserService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@Autowired
	public UserService(UserRepository userRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	@Override
	public void addUser(String userId, String globalName, String name, String langCode) {
		// TODO
	}

	@Override
	public void deleteUser(String userId) {
		// TODO
	}

	@Override
	public boolean userExists(String userId) {
		return false;
	}

	@Override
	public UserProfile getUserById(String id) {
		User user = userRepository.findById(id).orElse(null);
		if (user == null) return null;

		return userMapper.userToUserProfile(user);
	}

	@Override
	public long getUserCount() {
		return userRepository.count();
	}

	@Override
	public void setLanguage(String userId, String langCode) {
		// TODO
	}

	@Override
	public void addAdditionalLanguage(String userId, String langCode) {
		// TODO
	}

	@Override
	public void removeAdditionalLanguage(String userId, String langCode) {
		// TODO
	}

	@Override
	public Language getUserLanguage(String userId) {
		// TODO
		return null;
	}

	@Override
	public void addRole(String userId, String roleId) {
		// TODO
	}

	@Override
	public void removeRole(String userId, String roleId) {
		// TODO
	}

	@Override
	public List<String> getUsersByRole(String roleId) {
		// TODO
		return null;
	}

	@Override
	public boolean hasRole(String userId, String roleId) {
		// TODO
		return false;
	}
}
