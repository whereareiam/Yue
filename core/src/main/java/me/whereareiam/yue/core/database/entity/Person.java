package me.whereareiam.yue.core.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "yue_users")
@Getter
@Setter
@NoArgsConstructor
public class Person {
	@Id
	private int id;
	private String globalName;
	private String name;

	@ManyToMany
	private Set<PersonRole> personRoles;
}
