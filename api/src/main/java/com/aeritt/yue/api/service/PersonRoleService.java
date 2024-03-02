package com.aeritt.yue.api.service;

import java.util.List;

public interface PersonRoleService {
	void addRole(String userId, String roleId);

	void removeRole(String userId, String roleId);

	List<String> getPersonsByRole(String roleId);

	boolean hasRole(String userId, String roleId);
}