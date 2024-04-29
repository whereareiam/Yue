package com.aeritt.yue.api.event;

public interface EventManager {
	<T extends Event> void registerListener(EventListener<T> listener);

	<T extends Event> void unregisterListener(EventListener<T> listener);

	<T extends Event> void fireEvent(T event);
}
