package me.whereareiam.yue.database.repository;

import me.whereareiam.yue.database.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Integer> {
}
