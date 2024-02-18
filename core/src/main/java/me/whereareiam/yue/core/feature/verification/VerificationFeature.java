package me.whereareiam.yue.core.feature.verification;

import me.whereareiam.yue.core.config.feature.VerificationFeatureConfig;
import me.whereareiam.yue.core.config.setting.FeaturesSettingsConfig;
import me.whereareiam.yue.core.config.setting.SettingsConfig;
import me.whereareiam.yue.core.database.repository.PersonRepository;
import me.whereareiam.yue.core.feature.Feature;
import me.whereareiam.yue.core.model.StepData;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

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

				if (nextStepIndex == verificationSteps.size() - 1) {
					verifications.remove(userId);
				}
			}
		}

		verifications.put(userId, stepData);
		stepData.getStep().execute(stepData, buttonId);
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

		Guild guild = ctx.getBean(Guild.class);
		Logger logger = ctx.getBean(Logger.class);
		List<Member> members = guild.loadMembers().get();
		members.removeIf(member -> ctx.getBean(PersonRepository.class).findById(member.getId()).isPresent());
		members.remove(guild.getSelfMember());

		members.forEach(member -> {
			logger.info("Verifying member: " + member.getEffectiveName());
			verifyMember(member.getUser(), Optional.empty());
		});

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
