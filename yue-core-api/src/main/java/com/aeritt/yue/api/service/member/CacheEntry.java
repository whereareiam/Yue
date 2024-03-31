package com.aeritt.yue.api.service.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class CacheEntry {
	private final Member member;
	@Setter
	private Instant instant;
}
