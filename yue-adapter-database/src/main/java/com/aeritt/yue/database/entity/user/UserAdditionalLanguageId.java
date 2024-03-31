package com.aeritt.yue.database.entity.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class UserAdditionalLanguageId implements Serializable {
	private String userId;
	private int languageId;
}
