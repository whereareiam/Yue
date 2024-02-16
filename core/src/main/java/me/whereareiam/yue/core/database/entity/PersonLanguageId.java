package me.whereareiam.yue.core.database.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class PersonLanguageId implements Serializable {
	private String personId;
	private int languageId;
}
