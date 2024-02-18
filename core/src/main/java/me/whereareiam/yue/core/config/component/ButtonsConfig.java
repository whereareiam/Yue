package me.whereareiam.yue.core.config.component;

import lombok.Getter;
import me.whereareiam.yue.core.model.CustomButton;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ButtonsConfig {
	private List<CustomButton> buttons = new ArrayList<>();
}