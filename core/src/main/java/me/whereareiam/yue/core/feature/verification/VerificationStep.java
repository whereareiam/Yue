package me.whereareiam.yue.core.feature.verification;

import me.whereareiam.yue.core.config.feature.VerificationFeatureConfig;
import me.whereareiam.yue.core.model.StepData;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class VerificationStep {
	protected final VerificationFeatureConfig verificationConfig;
	protected final VerificationFeature verificationFeature;

	@Autowired
	public VerificationStep(VerificationFeatureConfig verificationConfig, VerificationFeature verificationFeature) {
		this.verificationConfig = verificationConfig;
		this.verificationFeature = verificationFeature;
	}

	protected abstract void execute(StepData stepData);

	protected abstract String getName();
}
