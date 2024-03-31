package com.aeritt.yue.api.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PlaceholderReplacement {
	private List<String> placeholders;
	private List<String> replacements;
}
