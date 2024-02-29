package com.aeritt.yue.core.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "yue_users")
@Getter
@Setter
@NoArgsConstructor
public class Person {
	@Id
	private String id;
	private String globalName;
	private String name;

	@ManyToOne
	@JoinColumn(name = "language_id")
	private Language mainLanguage;
}
