package me.whereareiam.yue.core.feature.verification;

import com.vdurmont.emoji.EmojiParser;
import me.whereareiam.yue.core.config.setting.FeaturesSettingsConfig;
import me.whereareiam.yue.core.config.setting.SettingsConfig;
import me.whereareiam.yue.core.database.repository.LanguageRepository;
import me.whereareiam.yue.core.feature.Feature;
import me.whereareiam.yue.core.util.message.MessageBuilderUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerificationFeature implements Feature {
	private final FeaturesSettingsConfig featuresSettingsConfig;
	private final LanguageRepository languageRepository;
	private final MessageBuilderUtil messageBuilderUtil;
	private boolean enabled;

	@Autowired
	public VerificationFeature(SettingsConfig settingsConfig, LanguageRepository languageRepository, MessageBuilderUtil messageBuilderUtil) {
		this.featuresSettingsConfig = settingsConfig.getFeatures();
		this.languageRepository = languageRepository;
		this.messageBuilderUtil = messageBuilderUtil;
	}

	public void verifyMember(Member member) {
		List<Button> buttons = languageRepository.findAll().stream()
				.map(language -> {
					Emoji emoji = Emoji.fromUnicode(EmojiParser.parseToUnicode(":flag_{code}:".replace("{code}", language.getCode())));
					return Button.success(language.getCode(), emoji);
				}).toList();

		MessageEmbed embed = messageBuilderUtil.buildEmbedMessage(
				member,
				"core.main.features.verification.step-1.title",
				"core.main.features.verification.step-1.content",
				"core.main.features.verification.step-1.subtitle"
		);

		member.getUser().openPrivateChannel()
				.flatMap(channel -> channel.sendMessageEmbeds(embed).setActionRow(buttons))
				.queue();
	}

	@Override
	public void initialize() {
		enabled = true;
	}

	@Override
	public void reload() {

	}

	@Override
	public boolean isEnabled() {
		return enabled == featuresSettingsConfig.isVerification();
	}
}
