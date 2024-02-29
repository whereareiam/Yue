package com.aeritt.yue.core.feature.verification;

import com.aeritt.yue.core.config.configs.feature.VerificationFeatureConfig;
import com.aeritt.yue.core.model.StepData;
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
