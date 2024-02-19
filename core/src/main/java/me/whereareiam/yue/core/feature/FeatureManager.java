package me.whereareiam.yue.core.feature;

import me.whereareiam.yue.api.event.ApplicationSuccessfullyStarted;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FeatureManager {
	private final ApplicationContext applicationContext;
	private final Map<Class<? extends Feature>, Feature> features = new HashMap<>();

	public FeatureManager(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Async
	@Order(1)
	@EventListener(ApplicationSuccessfullyStarted.class)
	public void loadFeatures() {
		String[] featureBeans = applicationContext.getBeanNamesForType(Feature.class);

		for (String beanName : featureBeans) {
			Feature feature = (Feature) applicationContext.getBean(beanName);
			feature.initialize();

			if (feature.isEnabled()) {
				features.put(feature.getClass(), feature);
			} else {
				applicationContext.getAutowireCapableBeanFactory().destroyBean(feature);
			}
		}
	}

	public Feature getFeature(Class<? extends Feature> featureName) {
		return features.get(featureName);
	}
}
