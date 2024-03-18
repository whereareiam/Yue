package com.aeritt.yue.api.event.database;

import com.aeritt.yue.api.event.Event;
import com.aeritt.yue.api.model.PersonBE;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserCreatedEvent extends Event {
	private final PersonBE person;
}
