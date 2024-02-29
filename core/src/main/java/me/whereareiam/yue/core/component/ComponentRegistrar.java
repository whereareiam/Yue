package me.whereareiam.yue.core.component;

import com.google.gson.*;
import me.whereareiam.yue.api.model.CustomButton;
import me.whereareiam.yue.api.model.CustomColor;
import me.whereareiam.yue.api.model.embed.Embed;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ComponentRegistrar {
	private final Logger logger;
	private final Path dataPath;
	private final SpringPluginManager pluginManager;
	private final ComponentService componentService;
	private final Gson gson = new Gson();

	private final Map<String, Function<JsonObject, Object>> componentParsers = new HashMap<>();

	@Autowired
	public ComponentRegistrar(Logger logger, @Qualifier("dataPath") Path dataPath, SpringPluginManager pluginManager, ComponentService componentService) {
		this.logger = logger;
		this.dataPath = dataPath;
		this.pluginManager = pluginManager;
		this.componentService = componentService;

		componentParsers.put("embed", this::parseEmbed);
		componentParsers.put("button", this::parseButton);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void registerComponents() {
		List<Path> componentPaths = pluginManager.getPlugins()
				.stream()
				.map(pluginWrapper -> pluginWrapper.getPluginPath().getParent().resolve(pluginWrapper.getPluginId() + "/component"))
				.collect(Collectors.toList());

		componentPaths.add(dataPath.resolve("component"));

		componentPaths
				.forEach(path -> {
					try (var paths = Files.walk(path)) {
						paths.filter(Files::isDirectory).forEach(dir -> {
							String dirName = dir.getFileName().toString();
							try (var files = Files.walk(dir, 1)) {
								files.filter(file -> file.toString().endsWith(".json")).forEach(file -> {
									String componentId;
									if (path.getNameCount() == 3) {
										componentId = "core." + dirName + "." + file.getFileName().toString().replace(".json", "");
									} else {
										componentId = file.subpath(3, 6).toString()
												.replace(".json", "")
												.replace("/", ".")
												.replace(".component.", ".")
												+ "." + file.getFileName().toString().replace(".json", "");
									}

									try (var reader = Files.newBufferedReader(file)) {
										registerComponent(dirName, componentId, reader);
									} catch (Exception e) {
										logger.warning("Failed to read component file: " + file);
										logger.warning(e.getMessage());
									}
								});
							} catch (Exception e) {
								logger.warning("Failed to read component directory: " + dir);
								logger.warning(e.getMessage());
							}
						});
					} catch (Exception e) {
						logger.warning("Failed to read plugin component: " + path);
						logger.warning(e.getMessage());
					}
				});
	}

	private void registerComponent(String type, String componentId, BufferedReader reader) {
		JsonElement jsonElement = gson.fromJson(reader, JsonElement.class);
		registerComponentsRecursively(jsonElement, componentId, null, type);
	}

	private void registerComponentsRecursively(JsonElement jsonElement, String componentId, String parentFieldName, String type) {
		if (jsonElement.isJsonObject()) {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			if (jsonObject.has("id") && componentParsers.containsKey(type)) {
				Object component = componentParsers.get(type).apply(jsonObject);
				String newComponentId = componentId + "." + (parentFieldName != null ? parentFieldName + "." : "") + jsonObject.get("id").getAsString();
				componentService.registerComponent(newComponentId, component);
			} else {
				for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
					registerComponentsRecursively(entry.getValue(), componentId, entry.getKey(), type);
				}
			}
		} else if (jsonElement.isJsonArray()) {
			JsonArray jsonArray = jsonElement.getAsJsonArray();
			for (JsonElement element : jsonArray) {
				registerComponentsRecursively(element, componentId, parentFieldName, type);
			}
		} else if (jsonElement.isJsonPrimitive()) {
			JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
			if (jsonPrimitive.isString() && type.equals("color")) {
				String colorString = jsonPrimitive.getAsString();
				CustomColor color = new CustomColor(colorString);
				String newComponentId = componentId + "." + parentFieldName;

				componentService.registerComponent(newComponentId, color);
			}
		}
	}

	private Embed parseEmbed(JsonObject jsonObject) {
		return gson.fromJson(jsonObject, Embed.class);
	}

	private CustomButton parseButton(JsonObject jsonObject) {
		return gson.fromJson(jsonObject, CustomButton.class);
	}
}