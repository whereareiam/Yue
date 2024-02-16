package me.whereareiam.yue.core.database.repository;

import me.whereareiam.yue.core.database.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {
	Optional<Language> findByCode(String langCode);
}