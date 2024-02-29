package com.aeritt.yue.core.feature.verification.steps;

import com.vdurmont.emoji.EmojiParser;
import com.aeritt.yue.api.discord.DiscordButtonManager;
import com.aeritt.yue.api.util.message.MessageBuilderUtil;
import com.aeritt.yue.core.config.configs.feature.VerificationFeatureConfig;
import com.aeritt.yue.core.config.configs.setting.SettingsConfig;
import com.aeritt.yue.core.database.entity.Language;
import com.aeritt.yue.core.database.repository.LanguageRepository;
import com.aeritt.yue.core.feature.verification.VerificationFeature;
import com.aeritt.yue.core.feature.verification.VerificationStep;
import com.aeritt.yue.core.model.StepData;
import com.aeritt.yue.core.service.PersonLanguageService;
import com.aeritt.yue.core.service.PersonService;
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
	private final MessageBuilderUtil messageBuilderUtil;
	private final LanguageRepository languageRepository;
	private final DiscordButtonManager buttonManager;
	private final SettingsConfig settingsConfig;
	private final PersonService personService;
	private final Logger logger;

	@Autowired
	public MainLanguageStep(PersonLanguageService personLanguageService, LanguageRepository languageRepository,
	                        DiscordButtonManager buttonManager, VerificationFeatureConfig verificationConfig,
	                        VerificationFeature verificationFeature, MessageBuilderUtil messageBuilderUtil,
	                        SettingsConfig settingsConfig, PersonService personService, Logger logger) {
		super(verificationConfig, verificationFeature);
		this.personLanguageService = personLanguageService;
		this.languageRepository = languageRepository;
		this.buttonManager = buttonManager;
		this.messageBuilderUtil = messageBuilderUtil;
		this.settingsConfig = settingsConfig;
		this.personService = personService;
		this.logger = logger;
	}

	@Override
	public void execute(StepData stepData) {
		MessageEmbed embed = messageBuilderUtil.embed(
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

		Button continueButton = messageBuilderUtil.button("continue", stepData.getUser());
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
