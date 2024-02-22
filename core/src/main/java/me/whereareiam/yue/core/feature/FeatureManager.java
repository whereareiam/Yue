package me.whereareiam.yue.core.feature;

import me.whereareiam.yue.api.event.ApplicationBotStarted;
import me.whereareiam.yue.api.event.ApplicationReloaded;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FeatureManager {
	private final ApplicationContext ctx;
	private final Map<Class<? extends Feature>, Feature> features = new HashMap<>();

	public FeatureManager(ApplicationContext ctx) {
		this.ctx = ctx;
	}

	@Async
	@EventListener(ApplicationBotStarted.class)
	public void loadFeatures() {
		String[] featureBeans = ctx.getBeanNamesForType(Feature.class);

		for (String beanName : featureBeans) {
			Feature feature = (Feature) ctx.getBean(beanName);
			feature.initialize();

			if (feature.isEnabled()) {
				features.put(feature.getClass(), feature);
			} else {
				ctx.getAutowireCapableBeanFactory().destroyBean(feature);
			}
		}
	}

	@EventListener(ApplicationReloaded.class)
	public void reloadFeatures() {
		features.values().forEach(Feature::reload);
	}

	public void notifyFeatures(Object event) {
		features.values().forEach(feature -> feature.handleEvent(event));
	}
}
