package me.whereareiam.yue.core.component;

import me.whereareiam.yue.api.model.CustomButton;
import me.whereareiam.yue.api.model.embed.Embed;
import me.whereareiam.yue.api.type.ColorType;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class ComponentService implements me.whereareiam.yue.api.component.ComponentService {
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
		String color = (String) getComponent(colorId);
		if (color == null) return getColorComponent(ColorType.FALLBACK);

		return Color.decode(color);
	}

	public Color getColorComponent(ColorType type) {
		String color = (String) getComponent("core.color.palette." + type.name().toLowerCase());
		if (color == null) return getColorComponent(ColorType.FALLBACK);

		return Color.decode(color);
	}

	private Object getComponent(String componentId) {
		return components.get(componentId.split("\\.")[0]).get(componentId);
	}

	public int getComponentCount() {
		return components.values().stream().mapToInt(Map::size).sum();
	}
}

