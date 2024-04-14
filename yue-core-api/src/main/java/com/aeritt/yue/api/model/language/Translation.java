package com.aeritt.yue.api.model.language;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class Translation {
	private String message;
	private List<String> placeholders;
	private List<Selector> selectors;

	public Translation(String message) {
		this.message = message;
		this.placeholders = new ArrayList<>();
		this.selectors = new ArrayList<>();

		Pattern placeholderPattern = Pattern.compile("(?<!\\$[ts])\\{[^}]+\\}");
		Matcher placeholderMatcher = placeholderPattern.matcher(message);
		while (placeholderMatcher.find()) {
			if (placeholders.contains(placeholderMatcher.group(0))) continue;
			placeholders.add(placeholderMatcher.group(0));
		}

		Pattern selectorPattern = Pattern.compile("\\$s\\{.*?'\\}");
		Matcher selectorMatcher = selectorPattern.matcher(message);
		while (selectorMatcher.find()) {
			selectors.add(new Selector(selectorMatcher.group()));
		}
	}

	public String process(List<Placeholder> placeholders) {
		String processedMessage = message;
		Map<String, String> placeholderMap = new HashMap<>();
		for (Placeholder placeholder : placeholders) {
			placeholderMap.put(placeholder.getKey(), placeholder.getValue());
		}

		for (Selector selector : selectors) {
			String selectedValue = selector.evaluate(placeholderMap);
			if (selectedValue != null) {
				processedMessage = processedMessage.replace(selector.toString(), selectedValue);
			}
		}

		for (Map.Entry<String, String> entry : placeholderMap.entrySet()) {
			processedMessage = processedMessage.replace(entry.getKey(), entry.getValue());
		}

		return processedMessage;
	}
}