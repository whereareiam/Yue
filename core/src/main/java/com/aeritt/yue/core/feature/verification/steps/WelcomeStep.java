package com.aeritt.yue.core.feature.verification.steps;

import com.aeritt.yue.api.util.message.MessageBuilderUtil;
import com.aeritt.yue.core.config.RolesConfig;
import com.aeritt.yue.core.config.configs.feature.VerificationFeatureConfig;
import com.aeritt.yue.core.database.repository.RoleRepository;
import com.aeritt.yue.core.feature.verification.VerificationFeature;
import com.aeritt.yue.core.feature.verification.VerificationStep;
import com.aeritt.yue.core.model.StepData;
import com.aeritt.yue.core.service.PersonRoleService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Lazy
@Service
public class WelcomeStep extends VerificationStep {
	private final MessageBuilderUtil messageBuilderUtil;
	private final PersonRoleService personRoleService;
	private final RoleRepository roleRepository;
	private final RolesConfig rolesConfig;
	private final Guild guild;

	@Autowired
	public WelcomeStep(VerificationFeatureConfig verificationConfig, VerificationFeature verificationFeature,
	                   MessageBuilderUtil messageBuilderUtil, PersonRoleService personRoleService,
	                   RoleRepository roleRepository, RolesConfig rolesConfig, @Lazy Guild guild) {
		super(verificationConfig, verificationFeature);
		this.messageBuilderUtil = messageBuilderUtil;
		this.personRoleService = personRoleService;
		this.roleRepository = roleRepository;
		this.rolesConfig = rolesConfig;
		this.guild = guild;
	}

	@Override
	public void execute(StepData stepData) {
		MessageEmbed embed = messageBuilderUtil.embed(
				"welcome",
				stepData.getUser(),
				Optional.empty()
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
				personRoleService.addRole(stepData.getUser().getId(), role);
				guild.addRoleToMember(stepData.getUser(), guildRole).queue();
			}
		}

		stepData.getMessage().delete().queue();
		stepData.getUser().openPrivateChannel().flatMap(
				privateChannel -> privateChannel.sendMessageEmbeds(embed)
		).queue(stepData::setMessage);
	}

	@Override
	public String getName() {
		return "verificationWelcome";
	}
}
