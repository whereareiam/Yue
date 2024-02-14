package me.whereareiam.yue.model;

import java.util.List;

public class Person {
	private final String id;
	private final String globalName;
	private String name;
	private List<Language> language;

	public Person(String id, String globalName, String name, List<Language> language) {
		this.id = id;
		this.globalName = globalName;
		this.name = name;
		this.language = language;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getGlobalName() {
		return globalName;
	}

	public List<Language> getLanguage() {
		return language;
	}

	public void setLanguage(List<Language> language) {
		this.language = language;
	}
}
