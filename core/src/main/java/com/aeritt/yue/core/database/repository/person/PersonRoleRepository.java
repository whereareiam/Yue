package com.aeritt.yue.core.database.repository.person;

import com.aeritt.yue.core.database.entity.Role;
import com.aeritt.yue.core.database.entity.person.Person;
import com.aeritt.yue.core.database.entity.person.PersonRole;
import com.aeritt.yue.core.database.entity.person.PersonRoleId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRoleRepository extends JpaRepository<PersonRole, PersonRoleId> {
	List<PersonRole> findByRole(Role role);

	List<PersonRole> findByPerson(Person person);

	@Modifying
	@Transactional
	void deleteByPersonAndRole(Person person, Role role);

	boolean existsByPersonIdAndRoleId(String userId, String id);
}
