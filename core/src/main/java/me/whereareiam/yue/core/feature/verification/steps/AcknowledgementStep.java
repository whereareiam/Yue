package me.whereareiam.yue.core.feature.verification.steps;

import me.whereareiam.yue.api.discord.DiscordButtonManager;
import me.whereareiam.yue.api.util.message.MessageBuilderUtil;
import me.whereareiam.yue.core.config.configs.feature.VerificationFeatureConfig;
import me.whereareiam.yue.core.feature.verification.VerificationFeature;
import me.whereareiam.yue.core.feature.verification.VerificationStep;
import me.whereareiam.yue.core.model.StepData;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Lazy
@Service
public class AcknowledgementStep extends VerificationStep {
	private final VerificationFeatureConfig verificationConfig;
	private final VerificationFeature verificationFeature;
	private final MessageBuilderUtil messageBuilderUtil;
	private final DiscordButtonManager buttonManager;

	public AcknowledgementStep(VerificationFeatureConfig verificationConfig, VerificationFeature verificationFeature,
	                           MessageBuilderUtil messageBuilderUtil, DiscordButtonManager buttonManager) {
		super(verificationConfig, verificationFeature);
		this.verificationConfig = verificationConfig;
		this.verificationFeature = verificationFeature;
		this.messageBuilderUtil = messageBuilderUtil;
		this.buttonManager = buttonManager;
	}

	@Override
	public void execute(StepData stepData) {
		MessageEmbed embed = messageBuilderUtil.embed(
				"acknowledgementCheck",
				stepData.getUser(),
				Optional.empty()
		);

		List<Button> buttons = List.of(
				messageBuilderUtil.button(verificationConfig.acknowledgement.getReadButtonId(), stepData.getUser())
						.withId(getName() + "-information"),
				messageBuilderUtil.button(verificationConfig.acknowledgement.getAcceptButtonId(), stepData.getUser())
						.withId(getName() + "-accept")
		);
		buttonManager.addButton(buttons.get(0).getId(), this::handleInformationButton);
		buttonManager.addButton(buttons.get(1).getId(), this::handleAcceptButton);


		stepData.setStep(this);
		stepData.getMessage().editMessageEmbeds(embed).setActionRow(buttons).queue();
	}

	private void handleInformationButton(ButtonInteractionEvent event) {
		StepData stepData = verificationFeature.getVerifications().get(event.getUser().getId());
		if (stepData.getStep() != this) return;

		stepData.getMessage().delete().queue();

		stepData.getUser().openPrivateChannel().flatMap(
				privateChannel -> privateChannel.sendMessageEmbeds(
						messageBuilderUtil.embed(
								"acknowledgementInformation",
								stepData.getUser(),
								Optional.empty()

						)
				)
		).queue();

		MessageEmbed embed = messageBuilderUtil.embed(
				"acknowledgementCheck",
				stepData.getUser(),
				Optional.empty()
		);

		Button button = messageBuilderUtil.button(verificationConfig.acknowledgement.getAcceptButtonId(), stepData.getUser())
				.withId(getName() + "-accept");

		stepData.getUser().openPrivateChannel().flatMap(
				privateChannel -> privateChannel.sendMessageEmbeds(embed).setActionRow(button)
		).queue(stepData::setMessage);
	}

	private void handleAcceptButton(ButtonInteractionEvent event) {
		StepData stepData = verificationFeature.getVerifications().get(event.getUser().getId());
		if (stepData.getStep() != this) return;

		stepData.setNextStep(true);
		verificationFeature.verifyMember(stepData.getUser());
	}

	@Override
	public String getName() {
		return "verificationAcknowledgement";
	}
}
