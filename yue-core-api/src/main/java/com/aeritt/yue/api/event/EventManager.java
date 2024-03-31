package com.aeritt.yue.api.event;

public interface EventManager {
	<T extends Event> void registerListener(Class<T> eventType, EventListener<T> listener);

	<T extends Event> void unregisterListener(Class<T> eventType, EventListener<T> listener);

	<T extends Event> void fireEvent(T event);
}
