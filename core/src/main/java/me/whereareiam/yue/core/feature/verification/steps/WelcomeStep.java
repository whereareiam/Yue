package me.whereareiam.yue.core.feature.verification.steps;

import me.whereareiam.yue.core.config.RolesConfig;
import me.whereareiam.yue.core.config.feature.VerificationFeatureConfig;
import me.whereareiam.yue.core.config.setting.SettingsConfig;
import me.whereareiam.yue.core.database.repository.RoleRepository;
import me.whereareiam.yue.core.database.service.PersonService;
import me.whereareiam.yue.core.feature.verification.VerificationFeature;
import me.whereareiam.yue.core.feature.verification.VerificationStep;
import me.whereareiam.yue.core.model.StepData;
import me.whereareiam.yue.core.util.message.MessageBuilderUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Lazy
public class WelcomeStep extends VerificationStep {
	private final RoleRepository roleRepository;
	private final SettingsConfig settingsConfig;
	private final PersonService personService;
	private final RolesConfig rolesConfig;
	private final Guild guild;

	@Autowired
	public WelcomeStep(VerificationFeatureConfig verificationConfig, VerificationFeature verificationFeature,
	                   MessageBuilderUtil messageBuilderUtil, RoleRepository roleRepository, SettingsConfig settingsConfig,
	                   PersonService personService, RolesConfig rolesConfig, Guild guild) {
		super(verificationConfig, verificationFeature, messageBuilderUtil);
		this.roleRepository = roleRepository;
		this.settingsConfig = settingsConfig;
		this.personService = personService;
		this.rolesConfig = rolesConfig;
		this.guild = guild;
	}

	@Override
	public void execute(StepData stepData, Optional<String> buttonId) {
		if (buttonId.isEmpty()) {
			MessageEmbed embed = messageBuilderUtil.successEmbed(
					stepData.getUser(),
					"core.main.feature.verification.welcome.title",
					"core.main.feature.verification.welcome.description",
					"core.main.feature.verification.welcome.footer"
			);

			stepData.setStep(this);
			stepData.setNextStep(true);
			stepData.getMessage().editMessageEmbeds(embed).queue();

			List<String> roles = stepData.getAdditionalLanguages().stream().map(
					language -> rolesConfig.getLanguageRoles().get(language.getCode())
			).collect(Collectors.toList());
			roles.add(rolesConfig.getLanguageRoles().get(stepData.getMainLanguage().getCode()));
			roles.add(rolesConfig.getVerifiedRole());

			if (roleRepository.existsById(roles)) {
				for (String role : roles) {
					Role guildRole = guild.getRoleById(role);
					personService.addRole(stepData.getUser().getId(), role);
					guild.addRoleToMember(stepData.getUser(), guildRole).queue();
				}
			}

			stepData.getMessage().delete().queue();
			stepData.getUser().openPrivateChannel().flatMap(
					privateChannel -> privateChannel.sendMessageEmbeds(embed)
			).queue(stepData::setMessage);
		}
	}

	@Override
	public String getName() {
		return "welcome";
	}
}
