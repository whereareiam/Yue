package com.aeritt.yue.database.entity.user;

import com.aeritt.yue.database.entity.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "yue_user_roles")
@Getter
@Setter
@NoArgsConstructor
@IdClass(UserRoleId.class)
public class UserRole {
	@Id
	@Column(name = "user_id")
	private String userId;

	@Id
	@Column(name = "role_id")
	private String roleId;

	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "role_id", insertable = false, updatable = false)
	private Role role;
}