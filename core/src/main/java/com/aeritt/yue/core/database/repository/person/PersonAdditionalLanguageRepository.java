package com.aeritt.yue.core.database.repository.person;

import com.aeritt.yue.core.database.entity.person.PersonAdditionalLanguage;
import com.aeritt.yue.core.database.entity.person.PersonAdditionalLanguageId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonAdditionalLanguageRepository extends JpaRepository<PersonAdditionalLanguage, PersonAdditionalLanguageId> {
	List<PersonAdditionalLanguage> findByPersonId(String personId);

	@Modifying
	@Transactional
	void deleteByPersonIdAndLanguageId(String personId, int languageId);
}

