package com.aeritt.yue.database.entity.user;

import com.aeritt.yue.database.entity.Language;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "yue_users")
@Getter
@Setter
@NoArgsConstructor
public class User {
	@Id
	private String id;
	private String globalName;
	private String name;

	@ManyToOne
	@JoinColumn(name = "language_id")
	private Language mainLanguage;
}
