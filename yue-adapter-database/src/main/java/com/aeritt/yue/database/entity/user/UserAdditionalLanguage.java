package com.aeritt.yue.database.entity.user;

import com.aeritt.yue.database.entity.Language;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "yue_user_languages")
@Getter
@Setter
@NoArgsConstructor
@IdClass(UserAdditionalLanguageId.class)
public class UserAdditionalLanguage {
	@Id
	@Column(name = "user_id")
	private String userId;

	@Id
	@Column(name = "language_id", length = 100)
	private int languageId;

	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "language_id", insertable = false, updatable = false)
	private Language language;
}