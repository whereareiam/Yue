package com.aeritt.yue.database.repository.user;

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
	void deleteByUserIdAndLanguageId(String userId, int languageId);
}

