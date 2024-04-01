package com.aeritt.yue.database.repository.user;

import com.aeritt.yue.database.entity.Role;
import com.aeritt.yue.database.entity.user.User;
import com.aeritt.yue.database.entity.user.UserRole;
import com.aeritt.yue.database.entity.user.UserRoleId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
	List<UserRole> findByRole(Role role);

	List<UserRole> findByUser(User user);

	@Modifying
	@Transactional
	void deleteByUserAndRole(User user, Role role);

	boolean existsByUserIdAndRoleId(String userId, String id);
}
