package me.whereareiam.yue.core.config.component;

import lombok.Getter;
import me.whereareiam.yue.core.model.Embed;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EmbedsConfig {
	List<Embed> embeds = new ArrayList<>();
}