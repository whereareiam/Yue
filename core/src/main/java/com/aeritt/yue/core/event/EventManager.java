package com.aeritt.yue.core.event;

import com.aeritt.yue.api.event.Event;
import com.aeritt.yue.api.event.EventListener;
import com.aeritt.yue.api.event.Priority;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class EventManager implements com.aeritt.yue.api.event.EventManager {
	private final Map<Class<? extends Event>, NavigableMap<Priority.Order, Set<EventListener<?>>>> listeners = new HashMap<>();
	private final ExecutorService executorService = Executors.newCachedThreadPool();

	public <T extends Event> void registerListener(Class<T> eventType, EventListener<T> listener) {
		Priority.Order defaultOrder = Priority.Order.NORMAL;
		for (Method method : listener.getClass().getDeclaredMethods()) {
			Priority.Order order = defaultOrder;
			if (method.isAnnotationPresent(Priority.class)) {
				Priority priorityAnnotation = method.getAnnotation(Priority.class);
				order = priorityAnnotation.value();
			}
			listeners.computeIfAbsent(eventType, k -> new TreeMap<>())
					.computeIfAbsent(order, k -> new HashSet<>())
					.add(listener);
		}
	}

	public <T extends Event> void unregisterListener(Class<T> eventType, EventListener<T> listener) {
		NavigableMap<Priority.Order, Set<EventListener<?>>> priorityMap = listeners.get(eventType);
		if (priorityMap != null) {
			for (Set<EventListener<?>> eventListeners : priorityMap.values()) {
				eventListeners.remove(listener);
			}
		}
	}

	public <T extends Event> void fireEvent(T event) {
		NavigableMap<Priority.Order, Set<EventListener<?>>> priorityMap = listeners.get(event.getClass());
		if (priorityMap != null) {
			for (Priority.Order order : Priority.Order.values()) {
				Set<EventListener<?>> eventListeners = priorityMap.get(order);
				if (eventListeners != null) {
					for (EventListener<?> listener : eventListeners) {
						executorService.submit(() -> {
							if (event.isCancelled()) {
								return;
							}
							((EventListener<T>) listener).onEvent(event);
						});
					}
				}
				if (event.isCancelled()) {
					break;
				}
			}
		}
	}
}
