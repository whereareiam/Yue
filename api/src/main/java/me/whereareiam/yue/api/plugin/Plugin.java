package me.whereareiam.yue.api.plugin;

import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringPlugin;

public abstract class Plugin extends SpringPlugin {
	public Plugin(PluginWrapper wrapper) {
		super(wrapper);
	}

	public abstract void onLoad();

	public abstract void onUnload();

	public abstract void onEnable();

	public abstract void onDisable();

	public abstract void onReload();
}
