package me.whereareiam.yue.database.repository;

import me.whereareiam.yue.database.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {
}
