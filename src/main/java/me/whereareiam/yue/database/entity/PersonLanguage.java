package me.whereareiam.yue.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "yue_user_languages")
@Getter
@Setter
@NoArgsConstructor
public class PersonLanguage {
	@Id
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Person person;

	@Id
	@ManyToOne
	@JoinColumn(name = "language_id")
	private Language language;
}
