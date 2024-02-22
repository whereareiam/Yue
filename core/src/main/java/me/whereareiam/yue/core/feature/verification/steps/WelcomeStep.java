package me.whereareiam.yue.core.feature.verification.steps;

import me.whereareiam.yue.core.config.RolesConfig;
import me.whereareiam.yue.core.config.feature.VerificationFeatureConfig;
import me.whereareiam.yue.core.database.repository.RoleRepository;
import me.whereareiam.yue.core.feature.verification.VerificationFeature;
import me.whereareiam.yue.core.feature.verification.VerificationStep;
import me.whereareiam.yue.core.model.StepData;
import me.whereareiam.yue.core.service.PersonRoleService;
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

@Lazy
@Service
public class WelcomeStep extends VerificationStep {
	private final RoleRepository roleRepository;
	private final PersonRoleService personRoleService;
	private final RolesConfig rolesConfig;
	private final Guild guild;

	@Autowired
	public WelcomeStep(VerificationFeatureConfig verificationConfig, VerificationFeature verificationFeature,
	                   RoleRepository roleRepository,
	                   PersonRoleService personRoleService, RolesConfig rolesConfig, Guild guild) {
		super(verificationConfig, verificationFeature);
		this.roleRepository = roleRepository;
		this.personRoleService = personRoleService;
		this.rolesConfig = rolesConfig;
		this.guild = guild;
	}

	@Override
	public void execute(StepData stepData) {
		MessageEmbed embed = MessageBuilderUtil.embed(
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
