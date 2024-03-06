package com.aeritt.yue.core.discord;

import com.aeritt.yue.api.discord.member.CacheEntry;
import com.aeritt.yue.api.discord.member.CacheReason;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DiscordMemberManager implements com.aeritt.yue.api.discord.member.DiscordMemberManager {
	private final Logger logger;
	private final Guild guild;

	private final Map<String, CacheEntry> members = new HashMap<>();

	@Autowired
	public DiscordMemberManager(Logger logger, @Lazy Guild guild) {
		this.logger = logger;
		this.guild = guild;
	}

	public void cacheMember(String id, CacheReason reason) {
		if (members.containsKey(id)) {
			members.get(id).setInstant(Instant.now().plusSeconds(5 * 60));
			return;
		}

		Member member = guild.retrieveMemberById(id).complete();
		if (member == null) return;

		switch (reason) {
			case COMMAND:
			case MESSAGE:
			case BUTTON:
			case JOIN:
				members.put(id,
						new CacheEntry(member, Instant.now().plusSeconds(5 * 60))
				);
				break;
			case MANUAL:
				members.put(id,
						new CacheEntry(member, Instant.now().plusSeconds(60))
				);
				break;
			default:
				member.getJDA().unloadUser(Long.parseLong(id));
				throw new IllegalArgumentException("Unknown reason: " + reason);
		}

		logger.log(Level.FINE, "Cached member: " + member.getEffectiveName());
	}

	public void cacheMember(String id, int seconds) {
		Member member = guild.retrieveMemberById(id).complete();
		if (member == null) return;

		members.put(id,
				new CacheEntry(member, Instant.now().plusSeconds(seconds))
		);

		logger.log(Level.FINE, "Cached member: " + member.getEffectiveName());
	}

	public void evictMember(String id) {
		members.remove(id);
	}

	@Async
	@Scheduled(fixedRate = 5 * 60 * 1000)
	public void evictExpiredMembers() {
		if (members.isEmpty()) return;

		logger.log(Level.FINE, "Cached members:");
		members.forEach((id, entry) -> logger.log(Level.FINE, " - " + entry.getMember().getEffectiveName()));

		Instant now = Instant.now();
		members.entrySet().removeIf(entry -> now.isAfter(entry.getValue().getInstant()));
	}

	public Member getMember(String id) {
		Member member = members.get(id).getMember();
		if (member == null) {
			cacheMember(id, CacheReason.MANUAL);
		}

		return member;
	}
}
