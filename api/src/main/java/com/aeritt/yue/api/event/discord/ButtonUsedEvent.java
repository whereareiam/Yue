package com.aeritt.yue.api.event.discord;

import com.aeritt.yue.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.User;

@Getter
@Setter
@AllArgsConstructor
public class ButtonUsedEvent extends Event {
	private final User user;
	private final String buttonId;
	private final String channelId;
	private final String messageId;
	private final boolean fromGuild;
	private final boolean isEphemeral;
}
