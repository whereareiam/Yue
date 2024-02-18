package me.whereareiam.yue.core.database.repository;

import jakarta.transaction.Transactional;
import me.whereareiam.yue.core.database.entity.Person;
import me.whereareiam.yue.core.database.entity.PersonRole;
import me.whereareiam.yue.core.database.entity.PersonRoleId;
import me.whereareiam.yue.core.database.entity.Role;
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
	void deleteByPersonIdAndRoleId(String userId, String roleId);
}
