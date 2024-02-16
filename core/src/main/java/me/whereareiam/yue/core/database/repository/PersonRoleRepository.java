package me.whereareiam.yue.core.database.repository;

import me.whereareiam.yue.core.database.entity.Person;
import me.whereareiam.yue.core.database.entity.PersonRole;
import me.whereareiam.yue.core.database.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRoleRepository extends JpaRepository<PersonRole, String> {
	List<PersonRole> findByRole(Role role);

	List<PersonRole> findByPerson(Person person);
}
