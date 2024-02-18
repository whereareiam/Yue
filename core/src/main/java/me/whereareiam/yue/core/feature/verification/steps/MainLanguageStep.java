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
import java.util.logging.Logger;

@Service
@Lazy
public class MainLanguageStep extends VerificationStep {
	private final Logger logger;
	private final LanguageRepository languageRepository;
	private final PersonService personService;

	@Autowired
	public MainLanguageStep(VerificationFeatureConfig verificationConfig, VerificationFeature verificationFeature,
	                        MessageBuilderUtil messageBuilderUtil, Logger logger, LanguageRepository languageRepository,
	                        PersonService personService) {
		super(verificationConfig, verificationFeature, messageBuilderUtil);
		this.logger = logger;
		this.languageRepository = languageRepository;
		this.personService = personService;
	}

	@Override
	public void execute(StepData stepData, Optional<String> buttonId) {
		if (buttonId.isEmpty()) {
			handleFirstJoin(stepData);
		} else {
			handleButtonPress(stepData, buttonId.get());
		}
	}

	private void handleFirstJoin(StepData stepData) {
		MessageEmbed embed = messageBuilderUtil.primaryEmbed(
				stepData.getUser(),
				"core.main.feature.verification.main.title",
				"core.main.feature.verification.main.description",
				"core.main.feature.verification.main.footer"
		);

		final List<Language> languages = languageRepository.findAll();
		List<Button> buttons = languages.stream().map(
				language -> Button.secondary(getName() + "-" + language.getCode(), Emoji.fromUnicode(
						EmojiParser.parseToUnicode(":flag_" + language.getCode() + ":")
				))
		).toList();

		stepData.setStep(this);
		stepData.getUser().openPrivateChannel()
				.flatMap(privateChannel -> privateChannel.sendMessageEmbeds(embed).setActionRow(buttons))
				.queue(
						stepData::setMessage,
						throwable -> logger.severe("Failed to send message to user: " + stepData.getUser().getAsMention())
				);

	}

	private void handleButtonPress(StepData stepData, String buttonId) {
		if (stepData.getStep() != this) return;

		buttonId = buttonId.replace(getName() + "-", "");

		Optional<Language> language = languageRepository.findByCode(buttonId);
		if (language.isEmpty()) return;

		stepData.setMainLanguage(language.get());
		stepData.setNextStep(true);

		personService.addUser(stepData.getUser().getId(), stepData.getUser().getGlobalName(), null, language.get().getCode());

		verificationFeature.verifyMember(stepData.getUser(), Optional.empty());
	}

	@Override
	public String getName() {
		return "main";
	}
}
