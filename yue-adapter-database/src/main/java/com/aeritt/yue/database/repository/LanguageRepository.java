package com.aeritt.yue.database.repository;

import com.aeritt.yue.database.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {
	Optional<Language> findByCode(String langCode);

	boolean existsByCode(String langCode);

	void deleteByCode(String langCode);
}