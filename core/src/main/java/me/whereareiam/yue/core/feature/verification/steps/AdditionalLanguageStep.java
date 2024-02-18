package me.whereareiam.yue.core.feature.verification.steps;

import com.vdurmont.emoji.EmojiParser;
import me.whereareiam.yue.core.config.feature.VerificationFeatureConfig;
import me.whereareiam.yue.core.database.entity.Language;
import me.whereareiam.yue.core.database.repository.LanguageRepository;
import me.whereareiam.yue.core.database.service.PersonService;
import me.whereareiam.yue.core.feature.verification.VerificationFeature;
import me.whereareiam.yue.core.feature.verification.VerificationStep;
import me.whereareiam.yue.core.model.StepData;
import me.whereareiam.yue.core.util.message.MessageBuilderUtil;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Lazy
public class AdditionalLanguageStep implements VerificationStep {
	private final VerificationFeatureConfig verificationConfig;
	private final VerificationFeature verificationFeature;
	private final MessageBuilderUtil messageBuilderUtil;
	private final LanguageRepository languageRepository;
	private final PersonService personService;

	@Autowired
	public AdditionalLanguageStep(VerificationFeatureConfig verificationConfig, VerificationFeature verificationFeature,
	                              MessageBuilderUtil messageBuilderUtil, LanguageRepository languageRepository,
	                              PersonService personService) {
		this.verificationConfig = verificationConfig;
		this.verificationFeature = verificationFeature;
		this.messageBuilderUtil = messageBuilderUtil;
		this.languageRepository = languageRepository;
		this.personService = personService;
	}

	@Override
	public void execute(StepData stepData, Optional<String> buttonId) {
		if (buttonId.isEmpty()) {
			updateEmbedMessage(stepData);
		} else {
			handleButtonPress(stepData, buttonId.get());
		}
	}

	private void updateEmbedMessage(StepData stepData) {
		MessageEmbed embed = messageBuilderUtil.primaryEmbed(
				stepData.getUser(),
				"core.main.feature.verification.additionalLanguage.title",
				"core.main.feature.verification.additionalLanguage.description",
				"core.main.feature.verification.additionalLanguage.footer"
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
				messageBuilderUtil.button(stepData.getUser(), verificationConfig.additionalLanguage.continueButtonId)
						.withId(getName() + "-continue")
		);

		stepData.setStep(this);
		stepData.getMessage().editMessageEmbeds(embed).setActionRow(buttons).queue();
	}

	private void handleButtonPress(StepData stepData, String buttonId) {
		if (stepData.getStep() != this) return;

		final List<Language> languages = languageRepository.findAll();

		if (buttonId.equalsIgnoreCase(getName() + "-continue")) {
			stepData.setNextStep(true);
			List<Language> additionalLanguages = stepData.getAdditionalLanguages();
			additionalLanguages.forEach(language -> personService.addAdditionalLanguage(stepData.getUser().getId(), language.getCode()));

			verificationFeature.verifyMember(stepData.getUser(), Optional.empty());
			return;
		}

		if (languages.stream().anyMatch(language -> buttonId.startsWith(getName() + "-" + language.getCode()))) {
			String langCode = buttonId.split("-")[1];
			Optional<Language> language = languages.stream().filter(lang -> lang.getCode().equals(langCode)).findFirst();
			language.ifPresent(stepData.getAdditionalLanguages()::add);

			verificationFeature.verifyMember(stepData.getUser(), Optional.empty());
		}
	}

	@Override
	public String getName() {
		return "additionalLanguage";
	}
}
