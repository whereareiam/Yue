package com.aeritt.yue.api.service;

public interface PersonService {
	void addUser(String userId, String globalName, String name, String langCode);

	void deleteUser(String userId);

	boolean userExists(String userId);

	long getUserCount();

	PersonLanguageService getPersonLanguageService();

	PersonRoleService getPersonRoleService();
}