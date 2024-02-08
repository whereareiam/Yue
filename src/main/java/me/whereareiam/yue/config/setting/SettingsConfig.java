package me.whereareiam.yue.config.setting;

import net.elytrium.serializer.language.object.YamlSerializable;
import org.springframework.stereotype.Component;

@Component
public class SettingsConfig extends YamlSerializable {
	public DiscordSettingsConfig discord = new DiscordSettingsConfig();
}
