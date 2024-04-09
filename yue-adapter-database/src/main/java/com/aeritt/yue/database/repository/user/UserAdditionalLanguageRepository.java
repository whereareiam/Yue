package com.aeritt.yue.database.repository.user;

import com.aeritt.yue.database.entity.Language;
import com.aeritt.yue.database.entity.user.User;
import com.aeritt.yue.database.entity.user.UserAdditionalLanguage;
import com.aeritt.yue.database.entity.user.UserAdditionalLanguageId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAdditionalLanguageRepository extends JpaRepository<UserAdditionalLanguage, UserAdditionalLanguageId> {
	List<UserAdditionalLanguage> findByUserId(String userId);

	@Modifying
	@Transactional
	void deleteByUserAndLanguage(User user, Language language);
}

