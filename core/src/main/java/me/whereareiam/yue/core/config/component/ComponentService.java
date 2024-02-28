package me.whereareiam.yue.core.config.component;

import me.whereareiam.yue.api.model.CustomButton;
import me.whereareiam.yue.api.model.Embed;
import me.whereareiam.yue.api.type.ColorType;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class ComponentService {
	private final Map<String, Map<String, Object>> components = new HashMap<>();

	public void registerComponent(String id, Object component) {
		String[] parts = id.split("\\.");
		String firstPart = parts[0];

		if (!components.containsKey(firstPart)) {
			components.put(firstPart, new HashMap<>());
		}

		components.get(firstPart).put(id, component);
	}

	public Embed getEmbedComponent(String embedId) {
		return (Embed) getComponent(embedId);
	}

	public CustomButton getButtonComponent(String buttonId) {
		return (CustomButton) getComponent(buttonId);
	}

	public Color getColorComponent(String colorId) {
		return Color.decode((String) getComponent(colorId));
	}

	public Color getColorComponent(ColorType type) {
		return Color.decode((String) getComponent("core.palette." + type.name()));
	}

	private Object getComponent(String componentId) {
		return components.get(componentId);
	}
}

