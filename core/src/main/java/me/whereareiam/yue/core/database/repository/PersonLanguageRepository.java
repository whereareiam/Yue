package me.whereareiam.yue.core.database.repository;

import me.whereareiam.yue.core.database.entity.PersonLanguage;
import me.whereareiam.yue.core.database.entity.PersonLanguageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonLanguageRepository extends JpaRepository<PersonLanguage, PersonLanguageId> {
	List<PersonLanguage> findByPersonId(String personId);
}
