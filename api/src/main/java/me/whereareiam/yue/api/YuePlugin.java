package me.whereareiam.yue.api;

import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringPlugin;
import org.springframework.context.ApplicationContext;

/*
 * YuePlugin is the base class for all plugins. It provides methods for loading, enabling, disabling, and unloading the plugin.
 *
 * The createApplicationContext method is used to create a new ApplicationContext for the plugin. This method is called before the onLoad method.
 *
 * @since 0.0.1
 */
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
