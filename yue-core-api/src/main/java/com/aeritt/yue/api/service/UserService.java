package com.aeritt.yue.api.service;

import com.aeritt.yue.api.model.Language;
import com.aeritt.yue.api.model.UserProfile;

import java.util.List;

public interface UserService {
	void addUser(String userId, String globalName, String name, String langCode);

	void deleteUser(String userId);

	boolean userExists(String userId);

	UserProfile getUserById(String id);

	long getUserCount();

	void setLanguage(String userId, String langCode);

	void addAdditionalLanguage(String userId, String langCode);

	void removeAdditionalLanguage(String userId, String langCode);

	Language getUserLanguage(String userId);

	void addRole(String userId, String roleId);

	void removeRole(String userId, String roleId);

	List<String> getUsersByRole(String roleId);

	boolean hasRole(String userId, String roleId);
}