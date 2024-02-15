package me.whereareiam.yue.core.database.repository;

import me.whereareiam.yue.core.database.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {
}