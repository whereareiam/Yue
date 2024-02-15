package me.whereareiam.yue.core.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "yue_user_roles")
@Getter
@Setter
@NoArgsConstructor
@IdClass(PersonRoleId.class)
public class PersonRole {
	@Id
	@Column(name = "user_id")
	private int personId;

	@Id
	@Column(name = "role_id")
	private int roleId;

	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private Person person;

	@ManyToOne
	@JoinColumn(name = "role_id", insertable = false, updatable = false)
	private Role role;
}
