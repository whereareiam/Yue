package com.aeritt.yue.core.service;

import com.aeritt.yue.api.event.EventManager;
import com.aeritt.yue.api.event.database.RoleAddedEvent;
import com.aeritt.yue.api.event.database.RoleRemovedEvent;
import com.aeritt.yue.core.database.entity.Role;
import com.aeritt.yue.core.database.entity.person.Person;
import com.aeritt.yue.core.database.entity.person.PersonRole;
import com.aeritt.yue.core.database.mapper.PersonMapper;
import com.aeritt.yue.core.database.repository.RoleRepository;
import com.aeritt.yue.core.database.repository.person.PersonRepository;
import com.aeritt.yue.core.database.repository.person.PersonRoleRepository;
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
	private final EventManager eventManager;

	private final PersonMapper personMapper;

	@Autowired
	public PersonRoleService(@Lazy PersonRepository personRepository, @Lazy RoleRepository roleRepository,
	                         @Lazy PersonRoleRepository personRoleRepository, EventManager eventManager, PersonMapper personMapper) {
		this.personRepository = personRepository;
		this.roleRepository = roleRepository;
		this.personRoleRepository = personRoleRepository;
		this.eventManager = eventManager;
		this.personMapper = personMapper;
	}

	public void addRole(String userId, String roleId) {
		Person person = personRepository.findById(userId).orElseThrow();
		Role role = roleRepository.findById(roleId).orElseThrow();

		RoleAddedEvent roleAddedEvent = new RoleAddedEvent(
				personMapper.personToPersonBE(person),
				role.getId()
		);
		eventManager.fireEvent(roleAddedEvent);
		if (roleAddedEvent.isCancelled()) return;

		PersonRole personRole = new PersonRole();
		personRole.setPersonId(person.getId());
		personRole.setRoleId(role.getId());
		personRoleRepository.save(personRole);
	}

	public void removeRole(String userId, String roleId) {
		Person person = personRepository.findById(userId).orElseThrow();
		Role role = roleRepository.findById(roleId).orElseThrow();

		RoleRemovedEvent roleRemovedEvent = new RoleRemovedEvent(
				personMapper.personToPersonBE(person),
				role.getId()
		);
		eventManager.fireEvent(roleRemovedEvent);
		if (roleRemovedEvent.isCancelled()) return;

		personRoleRepository.deleteByPersonAndRole(person, role);
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
