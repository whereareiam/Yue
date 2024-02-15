package me.whereareiam.yue.core.database.repository;

import me.whereareiam.yue.core.database.entity.PersonLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonLanguageRepository extends JpaRepository<PersonLanguage, Integer> {
}
