package me.whereareiam.yue.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "yue_languages")
@Getter
@Setter
@NoArgsConstructor
public class Language {
	@Id
	private int id;
	private String code;
	private String name;
}
