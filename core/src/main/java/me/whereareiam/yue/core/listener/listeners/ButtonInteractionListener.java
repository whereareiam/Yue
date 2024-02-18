package me.whereareiam.yue.core.listener.listeners;

import me.whereareiam.yue.core.config.setting.DiscordSettingsConfig;
import me.whereareiam.yue.core.config.setting.SettingsConfig;
import me.whereareiam.yue.core.feature.FeatureManager;
import me.whereareiam.yue.core.feature.verification.VerificationFeature;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ButtonInteractionListener extends ListenerAdapter {
	private final FeatureManager featureManager;
	private final DiscordSettingsConfig discordSettingsConfig;

	@Autowired
	public ButtonInteractionListener(FeatureManager featureManager, SettingsConfig settingsConfig) {
		this.featureManager = featureManager;
		this.discordSettingsConfig = settingsConfig.getDiscord();
	}

	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		if (!event.isFromGuild() || discordSettingsConfig.getGuildId().equals(event.getGuild().getId())) {
			VerificationFeature verificationFeature = (VerificationFeature) featureManager.getFeature(VerificationFeature.class);
			if (verificationFeature != null)
				verificationFeature.verifyMember(event.getUser(), Optional.of(event.getComponentId()));

			event.getInteraction().deferEdit().queue();
		}
	}
}
