package me.whereareiam.yue.api.service;

public interface PersonService {
	void addUser(String userId, String globalName, String name, String langCode);

	void deleteUser(String userId);

	long getUserCount();
}