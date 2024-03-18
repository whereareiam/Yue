package com.aeritt.yue.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PersonBE {
	private final String id;
	private final String globalName;
	private String name;
	private String mainLanguage;
}
