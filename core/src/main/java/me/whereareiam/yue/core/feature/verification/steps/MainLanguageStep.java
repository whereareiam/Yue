package me.whereareiam.yue.core.feature.verification.steps;

import com.vdurmont.emoji.EmojiParser;
import me.whereareiam.yue.core.config.feature.VerificationFeatureConfig;
import me.whereareiam.yue.core.database.entity.Language;
import me.whereareiam.yue.core.database.repository.LanguageRepository;
import me.whereareiam.yue.core.discord.DiscordButtonManager;
import me.whereareiam.yue.core.feature.verification.VerificationFeature;
import me.whereareiam.yue.core.feature.verification.VerificationStep;
import me.whereareiam.yue.core.model.StepData;
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

@Lazy
@Service
public class MainLanguageStep extends VerificationStep {
	private final Logger logger;
	private final LanguageRepository languageRepository;
	private final PersonService personService;
	private final DiscordButtonManager buttonManager;

	@Autowired
	public MainLanguageStep(VerificationFeatureConfig verificationConfig, VerificationFeature verificationFeature,
	                        Logger logger, LanguageRepository languageRepository, PersonService personService,
	                        DiscordButtonManager buttonManager) {
		super(verificationConfig, verificationFeature);
		this.logger = logger;
		this.languageRepository = languageRepository;
		this.personService = personService;
		this.buttonManager = buttonManager;
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
		).toList();
		buttons.forEach(button -> buttonManager.addButton(button.getId(), this::handleLanguageButton));

		stepData.setStep(this);
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
		stepData.setNextStep(true);

		personService.addUser(stepData.getUser().getId(), stepData.getUser().getGlobalName(), null, language.get().getCode());

		verificationFeature.verifyMember(stepData.getUser());
	}

	@Override
	public String getName() {
		return "verificationMain";
	}
}
