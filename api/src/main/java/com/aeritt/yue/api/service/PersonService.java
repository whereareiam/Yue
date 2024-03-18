package com.aeritt.yue.api.service;

import com.aeritt.yue.api.model.PersonBE;

public interface PersonService {
	void addUser(String userId, String globalName, String name, String langCode);

	void deleteUser(String userId);

	boolean userExists(String userId);

	PersonBE getPersonById(String id);

	long getUserCount();

	PersonLanguageService getPersonLanguageService();

	PersonRoleService getPersonRoleService();
}