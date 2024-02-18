package me.whereareiam.yue.core.feature.verification.steps;

import me.whereareiam.yue.core.config.feature.VerificationFeatureConfig;
import me.whereareiam.yue.core.feature.verification.VerificationFeature;
import me.whereareiam.yue.core.feature.verification.VerificationStep;
import me.whereareiam.yue.core.model.StepData;
import me.whereareiam.yue.core.util.message.MessageBuilderUtil;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Lazy
public class AcknowledgementStep implements VerificationStep {
	private final VerificationFeatureConfig verificationConfig;
	private final VerificationFeature verificationFeature;
	private final MessageBuilderUtil messageBuilderUtil;

	public AcknowledgementStep(VerificationFeatureConfig verificationConfig, VerificationFeature verificationFeature, MessageBuilderUtil messageBuilderUtil) {
		this.verificationConfig = verificationConfig;
		this.verificationFeature = verificationFeature;
		this.messageBuilderUtil = messageBuilderUtil;
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
				"core.main.feature.verification.acknowledgement.title",
				"core.main.feature.verification.acknowledgement.description",
				"core.main.feature.verification.acknowledgement.footer"
		);

		List<Button> buttons = List.of(
				messageBuilderUtil.button(stepData.getUser(), verificationConfig.acknowledgement.getGetInformationButtonId())
						.withId(getName() + "-information"),
				messageBuilderUtil.button(stepData.getUser(), verificationConfig.acknowledgement.getAcceptButtonId())
						.withId(getName() + "-accept")
		);

		stepData.setStep(this);
		stepData.getMessage().editMessageEmbeds(embed).setActionRow(buttons).queue();
	}

	private void handleButtonPress(StepData stepData, String buttonId) {
		if (stepData.getStep() != this) return;

		if (buttonId.equals(getName() + "-information")) {
			stepData.getMessage().delete().queue();

			stepData.getUser().openPrivateChannel().flatMap(
					privateChannel -> privateChannel.sendMessageEmbeds(
							messageBuilderUtil.dangerEmbed(
									stepData.getUser(),
									"core.acknowledgement.title",
									"core.acknowledgement.description",
									"core.acknowledgement.footer"

							)
					)
			).queue();

			MessageEmbed embed = messageBuilderUtil.primaryEmbed(
					stepData.getUser(),
					"core.main.feature.verification.acknowledgement.title",
					"core.main.feature.verification.acknowledgement.description",
					"core.main.feature.verification.acknowledgement.footer"
			);

			List<Button> buttons = List.of(
					messageBuilderUtil.button(stepData.getUser(), verificationConfig.acknowledgement.getAcceptButtonId())
							.withId(getName() + "-accept")
			);

			stepData.getUser().openPrivateChannel().flatMap(
					privateChannel -> privateChannel.sendMessageEmbeds(embed).setActionRow(buttons)
			).queue(stepData::setMessage);

			return;
		}

		if (buttonId.equals(getName() + "-accept")) {
			stepData.setNextStep(true);
			verificationFeature.verifyMember(stepData.getUser(), Optional.empty());
		}
	}

	@Override
	public String getName() {
		return "acknowledgement";
	}
}
