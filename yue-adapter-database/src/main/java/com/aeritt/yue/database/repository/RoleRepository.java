package com.aeritt.yue.database.repository;

import com.aeritt.yue.database.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
	default boolean existsByIds(List<String> ids) {
		for (String id : ids) {
			if (!existsById(id)) {
				return false;
			}
		}
		return true;
	}
}
