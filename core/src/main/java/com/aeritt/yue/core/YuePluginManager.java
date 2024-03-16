package com.aeritt.yue.core;

import com.aeritt.yue.api.YuePlugin;
import com.aeritt.yue.core.event.ApplicationBotStarted;
import com.aeritt.yue.core.event.ApplicationReloaded;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.pf4j.spring.ExtensionsInjector;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class YuePluginManager {
	private final SpringPluginManager pluginManager;
	private final ApplicationContext ctx;
	private final Path pluginPath;
	private final Logger logger;

	@Getter
	private final Set<YuePlugin> plugins = new HashSet<>();

	public YuePluginManager(SpringPluginManager pluginManager, ApplicationContext ctx, @Qualifier("pluginPath") Path pluginPath,
	                        Logger logger) {
		this.pluginManager = pluginManager;
		this.ctx = ctx;
		this.pluginPath = pluginPath;
		this.logger = logger;
	}

	@Order(Ordered.HIGHEST_PRECEDENCE)
	@EventListener(ApplicationReadyEvent.class)
	public void initialize() {
		File[] files = pluginPath.toFile().listFiles();
		if (files == null) return;

		for (File file : files) {
			if (file.isFile() && file.getName().endsWith(".jar")) {
				loadPlugin(file.toPath());
			}
		}

		AbstractAutowireCapableBeanFactory beanFactory = (AbstractAutowireCapableBeanFactory) ctx.getAutowireCapableBeanFactory();
		ExtensionsInjector extensionsInjector = new ExtensionsInjector(pluginManager, beanFactory);
		extensionsInjector.injectExtensions();
	}

	@EventListener(ApplicationReloaded.class)
	public void onReload() {
		plugins.forEach(this::reloadPlugin);
	}

	@PreDestroy
	public void onShutdown() {
		for (YuePlugin plugin : plugins) {
			disablePlugin(plugin);
			unloadPlugin(plugin);
		}
	}

	@EventListener(ApplicationBotStarted.class)
	public void enableAllPlugins() {
		plugins.forEach(this::enablePlugin);
	}

	private void loadPlugin(Path path) {
		YuePlugin plugin = null;
		String pluginId = "";

		try {
			pluginId = pluginManager.loadPlugin(path);
			plugin = (YuePlugin) pluginManager.getPlugin(pluginId).getPlugin();
			plugin.createApplicationContext();
			plugin.onLoad();
			plugins.add(plugin);
		} catch (Exception e) {
			logger.warning("Failed to load plugin " + path.getFileName());
			logger.warning(e.getMessage());
			plugins.remove(plugin);
		}
	}

	private void unloadPlugin(YuePlugin plugin) {
		try {
			plugins.remove(plugin);
			plugin.onUnload();
			((ConfigurableApplicationContext) plugin.getApplicationContext()).close();
			pluginManager.unloadPlugin(plugin.getWrapper().getPluginId());
		} catch (Exception e) {
			logger.warning("Failed to unload plugin " + plugin.getWrapper().getPluginId());
		}
	}

	private void enablePlugin(YuePlugin plugin) {
		if (!plugins.contains(plugin)) return;

		try {
			pluginManager.startPlugin(plugin.getWrapper().getPluginId());
			plugin.onEnable();
		} catch (Exception e) {
			plugins.remove(plugin);
			unloadPlugin(plugin);

			logger.warning("Failed to enable plugin " + plugin.getWrapper().getPluginId());
			logger.warning(e.getMessage());
		}
	}

	private void disablePlugin(YuePlugin plugin) {
		if (!plugins.contains(plugin)) return;

		try {
			plugin.onDisable();
			pluginManager.stopPlugin(plugin.getWrapper().getPluginId());
		} catch (Exception e) {
			unloadPlugin(plugin);
			logger.warning("Failed to disable plugin " + plugin.getWrapper().getPluginId());
		}
	}

	private void reloadPlugin(YuePlugin plugin) {
		try {
			plugin.onReload();
		} catch (Exception e) {
			logger.warning("Failed to reload plugin " + plugin.getWrapper().getPluginId());
		}
	}
}
