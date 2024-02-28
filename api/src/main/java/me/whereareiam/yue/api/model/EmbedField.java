package me.whereareiam.yue.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmbedField {
	private String name;
	private String value;
	private boolean inline;
}
