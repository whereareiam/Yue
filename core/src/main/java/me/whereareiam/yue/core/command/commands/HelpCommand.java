package me.whereareiam.yue.core.command.commands;

import me.whereareiam.yue.core.command.base.CommandBase;
import me.whereareiam.yue.core.command.base.CommandCategory;
import me.whereareiam.yue.core.command.management.CommandRegistrar;
import me.whereareiam.yue.core.config.command.CommandsConfig;
import me.whereareiam.yue.core.config.command.HelpCommandCommandsConfig;
import me.whereareiam.yue.core.discord.DiscordButtonManager;
import me.whereareiam.yue.core.util.message.MessageBuilderUtil;
import me.whereareiam.yue.core.util.message.MessageFormatterUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Lazy
@Component
public class HelpCommand extends CommandBase {
	private final HelpCommandCommandsConfig helpCommand;
	private final DiscordButtonManager buttonManager;
	private final CommandRegistrar commandRegistrar;
	private final Guild guild;

	private final List<CommandCategory> categories;

	@Autowired
	public HelpCommand(CommandsConfig commandsConfig, DiscordButtonManager buttonManager, CommandRegistrar commandRegistrar,
	                   Guild guild) {
		super(commandsConfig);
		this.helpCommand = commandsConfig.getHelpCommand();
		this.buttonManager = buttonManager;
		this.commandRegistrar = commandRegistrar;

		this.categories = commandRegistrar.getCommands().stream()
				.map(CommandBase::getCategory)
				.sorted(Comparator.comparing(Enum::name))
				.toList();
		this.guild = guild;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		if (event.getOption("command") != null) {
			event.getHook().sendMessage("Ты опциональный пидор").queue();
			return;
		}

		buildHelpMessage(event);
	}

	private void buildHelpMessage(SlashCommandInteractionEvent event) {
		User user = event.getUser();
		MessageEmbed embed = MessageBuilderUtil.embed("help", user, Optional.empty());
		List<CommandCategory> allowedCategories = getAllowedCategories(event.getMember());

		List<Button> buttons = allowedCategories.stream()
				.map(category -> MessageBuilderUtil.button(
						category.name().toLowerCase(),
						user)
				).toList();
		buttons.forEach(button -> buttonManager.addButton(button.getId(), this::buttonInteraction));

		List<MessageEmbed.Field> fields = allowedCategories.stream()
				.map(category ->
						MessageBuilderUtil.field(user,
								"core.commands.general.help.message.category." + category.name().toLowerCase() + ".name",
								"core.commands.general.help.message.category." + category.name().toLowerCase() + ".value",
								true
						)
				).toList();

		EmbedBuilder embedBuilder = new EmbedBuilder(embed);
		fields.forEach(embedBuilder::addField);

		embed = embedBuilder.build();
		event.getHook().sendMessageEmbeds(embed).setActionRow(buttons).queue();
	}

	private List<CommandCategory> getAllowedCategories(Member member) {
		return commandRegistrar.getCommands().stream()
				.filter(command -> member.getRoles().stream()
						.anyMatch(role -> role.getId().equals(command.getRequiredRole()))
				)
				.map(CommandBase::getCategory)
				.sorted(Comparator.comparing(Enum::name))
				.toList();
	}

	private void buttonInteraction(ButtonInteractionEvent event) {
		event.getMessage().editMessage("Ты пидор").setSuppressEmbeds(true).queue();
	}

	@Override
	public List<String> getCommandAliases() {
		return helpCommand.getCommand();
	}

	@Override
	public List<? extends CommandData> getCommand() {
		List<SlashCommandData> commandData = getCommandAliases().stream()
				.map(command ->
						Commands.slash(command, MessageFormatterUtil.formatMessage(helpCommand.getShortDescription()))
				)
				.toList();

		commandData.forEach(command -> {
			command.addOption(
					OptionType.STRING,
					"command",
					MessageFormatterUtil.formatMessage(
							helpCommand.getShortDescription().replace("shortDescription", "options.user.shortDescription")
					),
					false
			);

			command.setGuildOnly(isGuildOnly());
		});

		return commandData;
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.GENERAL;
	}

	@Override
	public String getRequiredRole() {
		return helpCommand.getRole();
	}

	@Override
	public List<String> getAllowedChannels() {
		return helpCommand.getAllowedChannels();
	}

	@Override
	public String getId() {
		return "helpCommand";
	}

	@Override
	public boolean isGuildOnly() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return helpCommand.isEnabled();
	}
}
