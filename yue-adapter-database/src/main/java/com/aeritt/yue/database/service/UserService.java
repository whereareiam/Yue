package com.aeritt.yue.database.service;

import com.aeritt.yue.api.model.Language;
import com.aeritt.yue.api.model.UserProfile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements com.aeritt.yue.api.service.UserService {
	@Override
	public void addUser(String userId, String globalName, String name, String langCode) {

	}

	@Override
	public void deleteUser(String userId) {

	}

	@Override
	public boolean userExists(String userId) {
		return false;
	}

	@Override
	public UserProfile getUserById(String id) {
		return null;
	}

	@Override
	public long getUserCount() {
		return 0;
	}

	@Override
	public void setLanguage(String userId, String langCode) {

	}

	@Override
	public void addAdditionalLanguage(String userId, String langCode) {

	}

	@Override
	public void removeAdditionalLanguage(String userId, String langCode) {

	}

	@Override
	public Language getUserLanguage(String userId) {
		return null;
	}

	@Override
	public void addRole(String userId, String roleId) {

	}

	@Override
	public void removeRole(String userId, String roleId) {

	}

	@Override
	public List<String> getUsersByRole(String roleId) {
		return null;
	}

	@Override
	public boolean hasRole(String userId, String roleId) {
		return false;
	}
}
