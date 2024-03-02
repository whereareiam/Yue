package com.aeritt.yue.core.service;

import com.aeritt.yue.core.database.entity.Person;
import com.aeritt.yue.core.database.entity.PersonRole;
import com.aeritt.yue.core.database.entity.Role;
import com.aeritt.yue.core.database.repository.PersonRepository;
import com.aeritt.yue.core.database.repository.PersonRoleRepository;
import com.aeritt.yue.core.database.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonRoleService implements com.aeritt.yue.api.service.PersonRoleService {
	private final PersonRepository personRepository;
	private final RoleRepository roleRepository;
	private final PersonRoleRepository personRoleRepository;

	@Autowired
	public PersonRoleService(@Lazy PersonRepository personRepository, @Lazy RoleRepository roleRepository,
	                         @Lazy PersonRoleRepository personRoleRepository) {
		this.personRepository = personRepository;
		this.roleRepository = roleRepository;
		this.personRoleRepository = personRoleRepository;
	}

	public void addRole(String userId, String roleId) {
		Person person = personRepository.findById(userId).orElseThrow();
		Role role = roleRepository.findById(roleId).orElseThrow();

		PersonRole personRole = new PersonRole();
		personRole.setPersonId(person.getId());
		personRole.setRoleId(role.getId());
		personRoleRepository.save(personRole);
	}

	public void removeRole(String userId, String roleId) {
		Role role = roleRepository.findById(roleId).orElseThrow();
		personRoleRepository.deleteByPersonIdAndRoleId(userId, role.getId());
	}

	public boolean hasRole(String userId, String roleId) {
		return personRoleRepository.existsByPersonIdAndRoleId(userId, roleId);
	}

	public List<String> getPersonsByRole(String roleId) {
		Optional<Role> role = roleRepository.findById(roleId);
		if (role.isPresent()) {
			List<PersonRole> personRoles = personRoleRepository.findByRole(role.get());
			List<String> personIds = new ArrayList<>();
			for (PersonRole personRole : personRoles) {
				personIds.add(personRole.getPersonId());
			}

			return personIds;
		} else {
			return new ArrayList<>();
		}
	}
}
