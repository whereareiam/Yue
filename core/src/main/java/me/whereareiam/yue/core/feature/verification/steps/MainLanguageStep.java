package me.whereareiam.yue.core.feature.verification.steps;

import com.vdurmont.emoji.EmojiParser;
import me.whereareiam.yue.core.database.entity.Language;
import me.whereareiam.yue.core.config.feature.VerificationFeatureConfig;
import me.whereareiam.yue.core.config.setting.SettingsConfig;
import me.whereareiam.yue.core.database.repository.LanguageRepository;
import me.whereareiam.yue.core.discord.DiscordButtonManager;
import me.whereareiam.yue.core.feature.verification.VerificationFeature;
import me.whereareiam.yue.core.feature.verification.VerificationStep;
import me.whereareiam.yue.core.model.StepData;
import me.whereareiam.yue.core.service.PersonLanguageService;
import me.whereareiam.yue.core.service.PersonService;
import me.whereareiam.yue.core.util.message.MessageBuilderUtil;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Lazy
@Service
public class MainLanguageStep extends VerificationStep {
	private final PersonLanguageService personLanguageService;
	private final LanguageRepository languageRepository;
	private final DiscordButtonManager buttonManager;
	private final SettingsConfig settingsConfig;
	private final PersonService personService;
	private final Logger logger;

	@Autowired
	public MainLanguageStep(PersonLanguageService personLanguageService, LanguageRepository languageRepository,
	                        DiscordButtonManager buttonManager, VerificationFeatureConfig verificationConfig,
	                        VerificationFeature verificationFeature, SettingsConfig settingsConfig,
	                        PersonService personService, Logger logger) {
		super(verificationConfig, verificationFeature);
		this.personLanguageService = personLanguageService;
		this.languageRepository = languageRepository;
		this.buttonManager = buttonManager;
		this.settingsConfig = settingsConfig;
		this.personService = personService;
		this.logger = logger;
	}

	@Override
	public void execute(StepData stepData) {
		MessageEmbed embed = MessageBuilderUtil.embed(
				"mainLanguage",
				stepData.getUser(),
				Optional.empty()
		);

		final List<Language> languages = languageRepository.findAll();
		List<Button> buttons = languages.stream().map(
				language -> Button.secondary(getName() + "-" + language.getCode(), Emoji.fromUnicode(
						EmojiParser.parseToUnicode(":flag_" + language.getCode() + ":")
				))
		).collect(Collectors.toList());
		buttons.forEach(button -> buttonManager.addButton(button.getId(), this::handleLanguageButton));

		Button continueButton = MessageBuilderUtil.button("continue", stepData.getUser());
		buttonManager.addButton(continueButton.getId(), this::handleContinueButton);
		buttons.add(continueButton);

		stepData.setMainLanguage(languages.stream().filter(
				language -> language.getCode().equals(settingsConfig.getDefaultLanguage())
		).findFirst().orElseThrow());
		stepData.setStep(this);
		personService.addUser(stepData.getUser().getId(), stepData.getUser().getGlobalName(), null, stepData.getMainLanguage().getCode());

		if (stepData.getMessage() != null) {
			stepData.getMessage().editMessageEmbeds(embed).setActionRow(buttons).queue();
			return;
		}
		stepData.getUser().openPrivateChannel()
				.flatMap(privateChannel -> privateChannel.sendMessageEmbeds(embed).setActionRow(buttons))
				.queue(
						stepData::setMessage,
						throwable -> logger.severe("Failed to send message to user: " + stepData.getUser().getAsMention())
				);
	}

	private void handleLanguageButton(ButtonInteractionEvent event) {
		StepData stepData = verificationFeature.getVerifications().get(event.getUser().getId());
		String buttonId = event.getComponentId();
		if (stepData.getStep() != this) return;

		buttonId = buttonId.replace(getName() + "-", "");

		Optional<Language> language = languageRepository.findByCode(buttonId);
		if (language.isEmpty()) return;

		stepData.setMainLanguage(language.get());

		personLanguageService.setLanguage(stepData.getUser().getId(), language.get().getCode());
		verificationFeature.verifyMember(stepData.getUser());
	}

	private void handleContinueButton(ButtonInteractionEvent event) {
		StepData stepData = verificationFeature.getVerifications().get(event.getUser().getId());
		String buttonId = event.getComponentId();
		if (stepData.getStep() != this) return;
		if (!buttonId.equals("continue")) return;

		stepData.setNextStep(true);
		verificationFeature.verifyMember(stepData.getUser());
	}

	@Override
	public String getName() {
		return "verificationMain";
	}
}
