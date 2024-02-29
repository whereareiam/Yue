package me.whereareiam.yue.api.component;

import me.whereareiam.yue.api.model.CustomButton;
import me.whereareiam.yue.api.model.embed.Embed;
import me.whereareiam.yue.api.type.ColorType;

import java.awt.*;

public interface ComponentService {
	void registerComponent(String id, Object component);

	Embed getEmbedComponent(String embedId);

	CustomButton getButtonComponent(String buttonId);

	Color getColorComponent(String colorId);

	Color getColorComponent(ColorType type);

	int getComponentCount();
}
