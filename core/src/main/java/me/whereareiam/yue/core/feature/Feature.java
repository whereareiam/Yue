package me.whereareiam.yue.core.feature;

public interface Feature {
	void initialize();

	void reload();

	boolean isEnabled();

	void handleEvent(Object object);
}
