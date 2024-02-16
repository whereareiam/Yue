package me.whereareiam.yue.core.database.repository;

import me.whereareiam.yue.core.database.entity.Person;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {
	Optional<Person> findById(@NotNull String id);
}
