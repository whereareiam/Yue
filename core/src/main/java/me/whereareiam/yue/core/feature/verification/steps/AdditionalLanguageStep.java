package me.whereareiam.yue.core.feature.verification.steps;

import com.vdurmont.emoji.EmojiParser;
import me.whereareiam.yue.core.database.entity.Language;
import me.whereareiam.yue.core.config.feature.VerificationFeatureConfig;
import me.whereareiam.yue.core.database.repository.LanguageRepository;
import me.whereareiam.yue.core.discord.DiscordButtonManager;
import me.whereareiam.yue.core.feature.verification.VerificationFeature;
import me.whereareiam.yue.core.feature.verification.VerificationStep;
import me.whereareiam.yue.core.model.StepData;
import me.whereareiam.yue.core.service.PersonLanguageService;
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
import java.util.stream.Collectors;

@Lazy
@Service
public class AdditionalLanguageStep extends VerificationStep {
	private final LanguageRepository languageRepository;
	private final PersonLanguageService personLanguageService;
	private final DiscordButtonManager buttonManager;

	@Autowired
	public AdditionalLanguageStep(VerificationFeatureConfig verificationConfig, VerificationFeature verificationFeature,
	                              LanguageRepository languageRepository, PersonLanguageService personLanguageService,
	                              DiscordButtonManager buttonManager) {
		super(verificationConfig, verificationFeature);
		this.languageRepository = languageRepository;
		this.personLanguageService = personLanguageService;
		this.buttonManager = buttonManager;
	}

	@Override
	public void execute(StepData stepData) {
		MessageEmbed embed = MessageBuilderUtil.embed(
				"additionalLanguage",
				stepData.getUser(),
				Optional.empty()
		);

		final List<Language> languages = languageRepository.findAll();
		List<Language> userLanguages = new java.util.ArrayList<>(languages);
		userLanguages.remove(stepData.getMainLanguage());
		userLanguages.removeAll(stepData.getAdditionalLanguages());

		List<Button> buttons = userLanguages.stream().map(
				language -> Button.secondary(getName() + "-" + language.getCode(), Emoji.fromUnicode(
						EmojiParser.parseToUnicode(":flag_" + language.getCode() + ":")
				))
		).collect(Collectors.toList());

		buttons.add(
				MessageBuilderUtil.button(verificationConfig.additionalLanguage.continueButtonId, stepData.getUser())
						.withId(getName() + "-continue")
		);
		buttons.forEach(button -> buttonManager.addButton(button.getId(), this::handleButtonPress));

		stepData.setStep(this);
		stepData.getMessage().editMessageEmbeds(embed).setActionRow(buttons).queue();
	}

	private void handleButtonPress(ButtonInteractionEvent event) {
		StepData stepData = verificationFeature.getVerifications().get(event.getUser().getId());
		String buttonId = event.getComponentId();
		if (stepData.getStep() != this) return;

		final List<Language> languages = languageRepository.findAll();

		if (buttonId.equalsIgnoreCase(getName() + "-continue")) {
			stepData.setNextStep(true);
			List<Language> additionalLanguages = stepData.getAdditionalLanguages();
			additionalLanguages.forEach(language -> personLanguageService.addAdditionalLanguage(stepData.getUser().getId(), language.getCode()));

			verificationFeature.verifyMember(stepData.getUser());
			return;
		}

		if (languages.stream().anyMatch(language -> buttonId.startsWith(getName() + "-" + language.getCode()))) {
			String langCode = buttonId.split("-")[1];
			Optional<Language> language = languages.stream().filter(lang -> lang.getCode().equals(langCode)).findFirst();
			language.ifPresent(stepData.getAdditionalLanguages()::add);

			verificationFeature.verifyMember(stepData.getUser());
		}
	}

	@Override
	public String getName() {
		return "verificationAdditionalLanguage";
	}
}
