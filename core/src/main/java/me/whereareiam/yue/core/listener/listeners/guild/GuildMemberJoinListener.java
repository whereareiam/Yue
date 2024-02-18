package me.whereareiam.yue.core.listener.listeners.guild;

import me.whereareiam.yue.core.config.setting.DiscordSettingsConfig;
import me.whereareiam.yue.core.config.setting.SettingsConfig;
import me.whereareiam.yue.core.database.repository.PersonRepository;
import me.whereareiam.yue.core.feature.FeatureManager;
import me.whereareiam.yue.core.feature.synchronization.RoleSynchronizerFeature;
import me.whereareiam.yue.core.feature.verification.VerificationFeature;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GuildMemberJoinListener extends ListenerAdapter {
	private final PersonRepository personRepository;
	private final FeatureManager featureManager;
	private final DiscordSettingsConfig discordSettingsConfig;

	@Autowired
	public GuildMemberJoinListener(PersonRepository personRepository, FeatureManager featureManager,
	                               SettingsConfig settingsConfig) {
		this.personRepository = personRepository;
		this.featureManager = featureManager;
		this.discordSettingsConfig = settingsConfig.getDiscord();
	}

	@Override
	public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
		if (discordSettingsConfig.getGuildId().equals(event.getGuild().getId())) {
			if (personRepository.findById(event.getUser().getId()).isPresent()) {
				((RoleSynchronizerFeature) featureManager.getFeature(RoleSynchronizerFeature.class)).synchronizeRoles(event.getMember());
				return;
			}

			VerificationFeature verificationFeature = (VerificationFeature) featureManager.getFeature(VerificationFeature.class);
			if (verificationFeature != null)
				verificationFeature.verifyMember(event.getMember().getUser(), Optional.empty());
		}
	}
}