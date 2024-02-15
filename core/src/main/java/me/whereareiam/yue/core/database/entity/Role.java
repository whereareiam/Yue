package me.whereareiam.yue.core.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "yue_roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {
	@Id
	private int id;
}
