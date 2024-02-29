package com.aeritt.yue.api.component;

import com.aeritt.yue.api.model.CustomButton;
import com.aeritt.yue.api.model.embed.Embed;
import com.aeritt.yue.api.type.ColorType;

import java.awt.*;

public interface ComponentService {
	void registerComponent(String id, Object component);

	Embed getEmbedComponent(String embedId);

	CustomButton getButtonComponent(String buttonId);

	Color getColorComponent(String colorId);

	Color getColorComponent(ColorType type);

	int getComponentCount();
}
