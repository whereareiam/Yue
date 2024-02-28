package me.whereareiam.yue.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

@Getter
@Setter
@AllArgsConstructor
public class CustomButton {
	private String id;
	private ButtonStyle style;
	private String label;
}