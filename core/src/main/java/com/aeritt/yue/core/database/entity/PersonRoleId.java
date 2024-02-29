package com.aeritt.yue.core.database.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class PersonRoleId implements Serializable {
	private String personId;
	private String roleId;
}
