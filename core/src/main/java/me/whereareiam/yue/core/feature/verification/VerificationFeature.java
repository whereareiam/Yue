package me.whereareiam.yue.core.feature.verification;

import me.whereareiam.yue.core.config.feature.VerificationFeatureConfig;
import me.whereareiam.yue.core.config.setting.FeaturesSettingsConfig;
import me.whereareiam.yue.core.config.setting.SettingsConfig;
import me.whereareiam.yue.core.feature.Feature;
import me.whereareiam.yue.core.model.StepData;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VerificationFeature implements Feature {
	private final ApplicationContext ctx;
	private final FeaturesSettingsConfig featuresSettingsConfig;
	private final VerificationFeatureConfig verificationFeatureConfig;
	private final List<VerificationStep> verificationSteps = new ArrayList<>();
	private final Map<String, StepData> verifications = new HashMap<>();
	private boolean enabled;

	@Autowired
	public VerificationFeature(@Qualifier ApplicationContext ctx, SettingsConfig settingsConfig,
	                           VerificationFeatureConfig verificationFeatureConfig) {
		this.ctx = ctx;
		this.featuresSettingsConfig = settingsConfig.getFeatures();
		this.verificationFeatureConfig = verificationFeatureConfig;
	}

	public void verifyMember(User user, Optional<String> buttonId) {
		String userId = user.getId();
		StepData stepData = verifications.get(userId);
		if (stepData == null) {
			stepData = new StepData(user, null, null, new ArrayList<>(), verificationSteps.get(0), false);
		}
		if (stepData.isNextStep()) {
			stepData.setNextStep(false);
			int nextStepIndex = verificationSteps.indexOf(stepData.getStep()) + 1;

			if (nextStepIndex < verificationSteps.size()) {
				stepData.setStep(verificationSteps.get(nextStepIndex));
			} else {
				registerMember(user);
				return;
			}
		}

		verifications.put(userId, stepData);
		stepData.getStep().execute(stepData, buttonId);
	}

	private void registerMember(User user) {
		verifications.remove(user.getId());

		System.out.println("Last step");
		//TODO: Implement this method
	}

	@Override
	public void initialize() {
		List<String> steps = verificationFeatureConfig.getSteps();
		for (String step : steps) {
			ctx.getBeansOfType(VerificationStep.class).values().stream()
					.filter(verificationStep -> verificationStep.getName().equals(step))
					.findFirst()
					.ifPresent(verificationSteps::add);
		}

		enabled = true;
	}

	@Override
	public void reload() {
		verifications.clear();
		initialize();
	}

	@Override
	public boolean isEnabled() {
		return enabled == featuresSettingsConfig.isVerification();
	}
}
