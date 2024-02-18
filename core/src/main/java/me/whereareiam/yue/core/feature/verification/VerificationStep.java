package me.whereareiam.yue.core.feature.verification;

import me.whereareiam.yue.core.model.StepData;

import java.util.Optional;

public interface VerificationStep {
	void execute(StepData stepData, Optional<String> buttonId);

	String getName();
}
