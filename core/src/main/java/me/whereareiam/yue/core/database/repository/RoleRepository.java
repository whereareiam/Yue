package me.whereareiam.yue.core.database.repository;

import me.whereareiam.yue.core.database.entity.Role;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
	boolean existsById(@NotNull String id);

	default boolean existsById(@NotNull List<String> ids) {
		for (String id : ids) {
			if (!existsById(id)) {
				return false;
			}
		}
		return true;
	}
}
