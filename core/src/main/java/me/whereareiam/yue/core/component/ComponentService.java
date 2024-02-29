package me.whereareiam.yue.core.component;

import me.whereareiam.yue.api.model.CustomButton;
import me.whereareiam.yue.api.model.CustomColor;
import me.whereareiam.yue.api.model.embed.Embed;
import me.whereareiam.yue.api.type.ColorType;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
		Optional<Object> component = getComponent(embedId);
		return (Embed) component.orElse(null);

	}

	public CustomButton getButtonComponent(String buttonId) {
		Optional<Object> component = getComponent(buttonId);
		return (CustomButton) component.orElse(null);
	}

	public Color getColorComponent(String colorId) {
		Optional<Object> component = getComponent(colorId);

		return component.map(object -> Color.decode(((CustomColor) object).getColor())).orElseGet(() -> getColorComponent(ColorType.FALLBACK));
	}

	public Color getColorComponent(ColorType type) {
		Optional<Object> component = getComponent("core.color.palette." + type.name().toLowerCase());

		return component.map(object -> Color.decode(((CustomColor) object).getColor())).orElseGet(() -> getColorComponent(ColorType.FALLBACK));
	}

	private Optional<Object> getComponent(String componentId) {
		try {
			return Optional.of(components.get(componentId.split("\\.")[0]).get(componentId));
		} catch (NullPointerException e) {
			return Optional.empty();
		}
	}

	public int getComponentCount() {
		return components.values().stream().mapToInt(Map::size).sum();
	}
}