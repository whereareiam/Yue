package me.whereareiam.yue.api.model.embed;

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
	private String color;
	private String url;
	private String image;
	private String thumbnail;
	private EmbedAuthor author;
	private EmbedFooter footer;
	private List<EmbedField> fields;

	public String[] getMessage() {
		List<String> messageParts = new ArrayList<>();

		if (title != null && !title.isEmpty()) {
			messageParts.add(title);
		}

		if (description != null && !description.isEmpty()) {
			messageParts.add(description);
		}

		if (footer != null && !footer.getText().isEmpty()) {
			messageParts.add(footer.getText());
		}

		return messageParts.toArray(new String[0]);
	}
}