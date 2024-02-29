package com.aeritt.yue.api.model.embed;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmbedField {
	private String name;
	private String value;
	private boolean inline;
}
