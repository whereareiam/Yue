package me.whereareiam.yue.api;

import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringPlugin;
import org.springframework.context.ApplicationContext;

public abstract class YuePlugin extends SpringPlugin {
	public YuePlugin(PluginWrapper wrapper) {
		super(wrapper);
	}

	public abstract void onLoad();

	public abstract void onUnload();

	public abstract void onEnable();

	public abstract void onDisable();

	public abstract void onReload();

	public abstract ApplicationContext createApplicationContext();
}
