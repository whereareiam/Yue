package me.whereareiam.yue.core.config.feature;

import lombok.Getter;

import java.util.List;

@Getter
public class VerificationFeatureConfig {
	public AdditionalLanguageStepConfig additionalLanguage = new AdditionalLanguageStepConfig();
	public AcknowledgementStepConfig acknowledgement = new AcknowledgementStepConfig();
	private List<String> steps = List.of(
			"main",
			"additionalLanguage",
			"acknowledgement"
	);
}
