package me.whereareiam.yue.core.discord;

import jakarta.annotation.PreDestroy;
import me.whereareiam.yue.api.event.ApplicationBotStarted;
import me.whereareiam.yue.core.config.RolesConfig;
import me.whereareiam.yue.core.config.setting.DiscordSettingsConfig;
import me.whereareiam.yue.core.config.setting.SettingsConfig;
import me.whereareiam.yue.core.database.entity.Role;
import me.whereareiam.yue.core.database.repository.RoleRepository;
import me.whereareiam.yue.core.language.LanguageService;
import me.whereareiam.yue.core.util.BeanRegistrationUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DependsOn("databaseSetupManager")
public class DiscordSetupManager {
	private final BeanRegistrationUtil beanRegistrationUtil;
	private final LanguageService languageService;
	private final SettingsConfig settingsConfig;
	private final RolesConfig rolesConfig;
	private final RoleRepository roleRepository;
	private JDA jda;

	@Autowired
	public DiscordSetupManager(BeanRegistrationUtil beanRegistrationUtil, LanguageService languageService, SettingsConfig settingsConfig,
	                           RolesConfig rolesConfig, RoleRepository roleRepository) {
		this.beanRegistrationUtil = beanRegistrationUtil;
		this.languageService = languageService;
		this.settingsConfig = settingsConfig;
		this.rolesConfig = rolesConfig;
		this.roleRepository = roleRepository;
	}

	@Order(Ordered.HIGHEST_PRECEDENCE)
	@EventListener(ApplicationReadyEvent.class)
	public void onLoad() {
		try {
			DiscordSettingsConfig discordSettingsConfig = settingsConfig.getDiscord();
			JDABuilder bot = JDABuilder
					.createDefault(discordSettingsConfig.getToken())
					.enableIntents(discordSettingsConfig.getIntents())
					.setMemberCachePolicy(MemberCachePolicy.ONLINE)
					.setAutoReconnect(discordSettingsConfig.isAutoReconnect());

			jda = bot.build();
			beanRegistrationUtil.registerSingleton("jda", JDA.class, jda);
		} catch (InvalidTokenException e) {
			throw new RuntimeException("Discord token is invalid. Please check your settings.json file.");
		}

		jda.getPresence().setActivity(Activity.playing(
				languageService.getTranslation("core.main.phase.loading")
		));

		registerRoles();
	}

	private void registerRoles() {
		List<Role> existingRoles = roleRepository.findAll();

		List<String> existingRoleIds = existingRoles.stream()
				.map(Role::getId)
				.toList();

		existingRoles.stream()
				.filter(role -> !rolesConfig.getRoles().contains(role.getId()))
				.forEach(roleRepository::delete);

		rolesConfig.getRoles().stream()
				.filter(roleId -> !existingRoleIds.contains(roleId))
				.forEach(roleId -> {
					Role newRole = new Role();
					newRole.setId(roleId);
					roleRepository.save(newRole);
				});
	}

	@Order(Ordered.HIGHEST_PRECEDENCE)
	@EventListener(ApplicationBotStarted.class)
	public void onEnable() {
		beanRegistrationUtil.registerSingleton("guild", Guild.class, jda.getGuildById(settingsConfig.getDiscord().getGuildId()));
		jda.getPresence().setActivity(Activity.playing(
				languageService.getTranslation("core.main.phase.starting")
		));
	}

	@PreDestroy
	public void onDisable() {
		jda.shutdown();
	}
}
