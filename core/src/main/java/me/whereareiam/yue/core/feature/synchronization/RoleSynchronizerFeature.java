package me.whereareiam.yue.core.feature.synchronization;

import me.whereareiam.yue.core.config.setting.FeaturesSettingsConfig;
import me.whereareiam.yue.core.config.setting.SettingsConfig;
import me.whereareiam.yue.core.database.entity.Person;
import me.whereareiam.yue.core.database.entity.PersonRole;
import me.whereareiam.yue.core.database.entity.Role;
import me.whereareiam.yue.core.database.repository.PersonRepository;
import me.whereareiam.yue.core.database.repository.PersonRoleRepository;
import me.whereareiam.yue.core.feature.Feature;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class RoleSynchronizerFeature implements Feature {
	private final Logger logger;
	private final Guild guild;
	private final FeaturesSettingsConfig featuresSettingsConfig;
	private final PersonRepository personRepository;
	private final PersonRoleRepository personRoleRepository;
	private boolean enabled;

	@Autowired
	public RoleSynchronizerFeature(Logger logger, @Lazy Guild guild, SettingsConfig settingsConfig, PersonRepository personRepository,
	                               PersonRoleRepository personRoleRepository) {
		this.logger = logger;
		this.guild = guild;
		this.featuresSettingsConfig = settingsConfig.getFeatures();
		this.personRepository = personRepository;
		this.personRoleRepository = personRoleRepository;
	}

	public void synchronizeRoles() {
		List<Member> members = guild.loadMembers().get();
		for (Member member : members) {
			if (member.getUser().isBot() || !guild.getSelfMember().canInteract(member))
				continue;

			try {
				processMember(member);
			} catch (Exception e) {
				logger.severe("Failed to process member: " + member.getEffectiveName());
			}
		}

		logger.info("Discord role synchronization completed.");
	}

	private void processMember(Member member) {
		Optional<Person> personOptional = personRepository.findById(member.getId());
		if (personOptional.isPresent()) {
			Person person = personOptional.get();
			List<PersonRole> personRoles = personRoleRepository.findByPerson(person);
			List<Role> rolesInDb = personRoles.stream().map(PersonRole::getRole).toList();

			removeExtraRoles(member, rolesInDb);
			addMissingRoles(member, rolesInDb);
		} else {
			removeAllRoles(member);
		}
	}

	private void removeExtraRoles(Member member, List<Role> rolesInDb) {
		List<net.dv8tion.jda.api.entities.Role> rolesToRemove = member.getRoles().stream()
				.filter(role -> !rolesInDb.stream().map(Role::getId).toList().contains(role.getId()))
				.toList();

		rolesToRemove.forEach(role -> guild.removeRoleFromMember(member, role).queue());
	}

	private void addMissingRoles(Member member, List<Role> rolesInDb) {
		rolesInDb.stream()
				.filter(role -> member.getRoles().stream().noneMatch(discordRole -> discordRole.getId().equals(role.getId())))
				.forEach(role -> {
					net.dv8tion.jda.api.entities.Role discordRole = guild.getRoleById(role.getId());
					if (discordRole != null) {
						guild.addRoleToMember(member, discordRole).queue();
					} else {
						logger.warning("Role not found for ID: " + role.getId());
					}
				});
	}

	private void removeAllRoles(Member member) {
		member.getRoles().forEach(role -> guild.removeRoleFromMember(member, role).queue());
	}

	@Override
	public void initialize() {
		enabled = true;

		if (featuresSettingsConfig.isSynchronization()) {
			synchronizeRoles();
		}
	}

	@Override
	public void reload() {
		synchronizeRoles();
	}

	@Override
	public boolean isEnabled() {
		return enabled == featuresSettingsConfig.isSynchronization();
	}
}
