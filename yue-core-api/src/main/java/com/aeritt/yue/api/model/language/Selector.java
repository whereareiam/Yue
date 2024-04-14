package com.aeritt.yue.api.model.language;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Selector {
	private List<String> conditions;
	private List<String> values;

	public Selector(String selectorString) {
		this.conditions = new ArrayList<>();
		this.values = new ArrayList<>();

		selectorString = selectorString.substring(4, selectorString.length() - 2);

		String[] parts = selectorString.split(";", 2);
		String conditionsPart = parts[0];
		String valuesPart = parts.length > 1 ? parts[1] : "";

		String[] conditionsArray = conditionsPart.split("\\|");
		String[] valuesArray = valuesPart.split("\\|");

		Collections.addAll(this.conditions, conditionsArray);
		Collections.addAll(this.values, valuesArray);
	}

	public String evaluate(List<Placeholder> placeholders) {
		System.out.println("Evaluating selector: " + conditions + " -> " + values);

		for (int i = 0; i < conditions.size(); i++) {
			String condition = conditions.get(i);
			String value = values.get(i);

			for (Placeholder placeholder : placeholders) {
				if (condition.contains(placeholder.getKey()))
					condition = condition.replace(placeholder.getKey(), placeholder.getValue());
			}

			System.out.println("Checking condition: " + condition + " -> " + value);

			String[] parts = condition.split("([<>!=]=?)");
			double left = Double.parseDouble(parts[0].trim());
			double right = Double.parseDouble(parts[1].trim());

			if (condition.contains("<=") && left <= right
					|| condition.contains(">=") && left >= right
					|| condition.contains("==") && left == right
					|| condition.contains("!=") && left != right
					|| condition.contains("<") && left < right
					|| condition.contains(">") && left > right) {
				return value;
			}
		}

		return null;
	}

	@Override
	public String toString() {
		return "$s{'" + String.join("|", conditions) + ";" + String.join("|", values) + "'}";
	}
}
