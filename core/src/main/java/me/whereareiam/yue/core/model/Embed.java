package me.whereareiam.yue.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class Embed {
	private String id;
	private String title;
	private String description;
	private String footer;
	private String color;
	private List<EmbedField> fields;

	public String[] getMessage() {
		List<String> messageParts = new ArrayList<>();

		if (title != null && !title.isEmpty()) {
			messageParts.add(title);
		}

		if (description != null && !description.isEmpty()) {
			messageParts.add(description);
		}

		if (footer != null && !footer.isEmpty()) {
			messageParts.add(footer);
		}

		return messageParts.toArray(new String[0]);
	}
}
