package me.whereareiam.yue.api;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface Scheduler {
	ScheduledFuture<?> scheduleAtDateTime(Runnable command, LocalDateTime dateTime);

	ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit, Optional<Integer> repetitions);

	ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit);

	void cancelTask(ScheduledFuture<?> future);
}
