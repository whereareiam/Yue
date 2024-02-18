package me.whereareiam.yue.core.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "yue_languages")
@Getter
@Setter
@NoArgsConstructor
public class Language {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String code;
	private String name;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Language language = (Language) obj;
		return Objects.equals(code, language.code);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

}
