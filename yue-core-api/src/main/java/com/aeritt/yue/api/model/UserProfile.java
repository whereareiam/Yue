package com.aeritt.yue.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserProfile {
	private String id;
	private Language language;
	private List<Language> additionalLanguages;
	private List<String> roles;
	private String globalName;
	private String name;
}
