package me.whereareiam.yue.api.module;

public interface Module {
	void onLoad();

	void onUnload();

	void onEnable();

	void onDisable();

	void onReload();
}
