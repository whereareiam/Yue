package com.aeritt.yue.api.service.member;

import net.dv8tion.jda.api.entities.Member;

public interface DiscordMemberService {
	void cacheMember(String id, CacheReason reason);

	void cacheMember(String id, int seconds);

	void evictMember(String id);

	void evictExpiredMembers();

	Member getMember(String id);
}
