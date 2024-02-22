package me.whereareiam.yue.core.config.component.palette;

import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Getter
public class PaletteConfig {
	private String fallback = "#000000";
	private MainPaletteConfig main = new MainPaletteConfig();

	public String getColor(String key) {
		if (key.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
			return key;
		}

		String[] parts = key.split("\\.", 2);
		if (parts.length < 2) {
			throw new IllegalArgumentException("Key must be in format 'section.field'");
		}

		String section = parts[0];
		String field = parts[1];

		try {
			Field sectionField = this.getClass().getDeclaredField(section);
			Object sectionObj = sectionField.get(this);
			return getColorRecursive(sectionObj, field);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			return fallback;
		}
	}

	private String getColorRecursive(Object obj, String key) {
		String[] parts = key.split("\\.", 2);
		try {
			String fieldName = parts[0];

			String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Method getterMethod = obj.getClass().getMethod(getterName);

			if (parts.length > 1) {
				return getColorRecursive(getterMethod.invoke(obj), parts[1]);
			} else {
				return (String) getterMethod.invoke(obj);
			}
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			return fallback;
		}
	}

}
