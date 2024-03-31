package com.aeritt.yue.database.entity.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class UserRoleId implements Serializable {
	private String userId;
	private String roleId;
}
