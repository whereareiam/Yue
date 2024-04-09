package com.aeritt.yue.api.service;

import com.aeritt.yue.api.model.Language;
import com.aeritt.yue.api.model.UserProfile;

import java.util.List;
import java.util.Optional;

public interface UserService {
	boolean addUser(String userId, String globalName, String name, String langCode);

	boolean deleteUser(String userId);

	boolean userExists(String userId);

	Optional<UserProfile> getUserById(String id);

	long getUserCount();

	boolean setLanguage(String userId, String langCode);

	boolean addAdditionalLanguage(String userId, String langCode);

	boolean removeAdditionalLanguage(String userId, String langCode);

	Language getUserLanguage(String userId);

	boolean addRole(String userId, String roleId);

	boolean removeRole(String userId, String roleId);

	List<String> getUsersByRole(String roleId);

	boolean hasRole(String userId, String roleId);
}