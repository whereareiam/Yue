package me.whereareiam.yue.core.feature.verification;

import me.whereareiam.yue.core.config.feature.VerificationFeatureConfig;
import me.whereareiam.yue.core.model.StepData;
import me.whereareiam.yue.core.util.message.MessageBuilderUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public abstract class VerificationStep {
	protected final VerificationFeatureConfig verificationConfig;
	protected final VerificationFeature verificationFeature;
	protected final MessageBuilderUtil messageBuilderUtil;

	@Autowired
	public VerificationStep(VerificationFeatureConfig verificationConfig, VerificationFeature verificationFeature,
	                        MessageBuilderUtil messageBuilderUtil) {
		this.verificationConfig = verificationConfig;
		this.verificationFeature = verificationFeature;
		this.messageBuilderUtil = messageBuilderUtil;
	}

	protected abstract void execute(StepData stepData, Optional<String> buttonId);

	protected abstract String getName();
}
