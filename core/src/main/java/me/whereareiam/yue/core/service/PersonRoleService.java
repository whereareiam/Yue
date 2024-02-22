package me.whereareiam.yue.core.service;

import me.whereareiam.yue.core.database.entity.Person;
import me.whereareiam.yue.core.database.entity.PersonRole;
import me.whereareiam.yue.core.database.entity.Role;
import me.whereareiam.yue.core.database.repository.PersonRepository;
import me.whereareiam.yue.core.database.repository.PersonRoleRepository;
import me.whereareiam.yue.core.database.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonRoleService {
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

	public List<PersonRole> getPersonsByRole(String roleId) {
		Optional<Role> role = roleRepository.findById(roleId);
		if (role.isPresent()) {
			return personRoleRepository.findByRole(role.get());
		} else {
			return new ArrayList<>();
		}
	}
}
