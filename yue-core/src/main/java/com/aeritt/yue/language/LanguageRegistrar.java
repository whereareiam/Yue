package com.aeritt.yue.language;

import com.aeritt.yue.api.model.Language;
import com.aeritt.yue.api.service.LanguageService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Service
public class LanguageRegistrar {
	private final Gson gson;
	private final Logger logger;
	private final Path langPath;
	private final LanguageProvider languageProvider;
	private final LanguageService languageService;

	@Autowired
	public LanguageRegistrar(Gson gson, Logger logger, @Qualifier("languagePath") Path langPath,
	                         LanguageProvider languageProvider, LanguageService languageService) {
		this.gson = gson;
		this.logger = logger;
		this.languageProvider = languageProvider;
		this.langPath = langPath;
		this.languageService = languageService;
	}

	@Async
	@Order(Ordered.HIGHEST_PRECEDENCE)
	@EventListener(ApplicationStartedEvent.class)
	public void registerTranslations() {
		try (Stream<Path> paths = Files.walk(langPath, 1)) {
			paths.filter(Files::isDirectory).forEach(dir -> {
				String dirName = dir.getFileName().toString();
				String defaultFile = dir.resolve(languageService.getDefaultLanguage().getCode() + ".json").toString();
				try (Stream<Path> files = Files.walk(dir, 1)) {
					files.filter(file -> file.toString().endsWith(".json")).forEach(file -> {
						String langCode = file.getFileName().toString().replace(".json", "");
						if (!defaultFile.equals(file.toString())) {
							mergeJsonFiles(defaultFile, file.toString());
						}
						processLanguageFile(file, dirName, langCode);
					});
				} catch (IOException e) {
					logger.warning("Failed to read language directory: " + dir);
				}
			});
		} catch (IOException e) {
			logger.warning("Failed to read language directory: " + langPath);
		}
	}

	private void mergeJsonFiles(String defaultFile, String targetFile) {
		JsonObject defaultJson = getJsonObjectFromFile(defaultFile);
		JsonObject targetJson = getJsonObjectFromFile(targetFile);
		mergeJsonObjects(defaultJson, targetJson);
		try (Writer writer = Files.newBufferedWriter(Paths.get(targetFile))) {
			gson.toJson(targetJson, writer);
		} catch (IOException e) {
			logger.warning("Failed to write language file: " + targetFile);
		}
	}

	private void processLanguageFile(Path file, String dirName, String langCode) {
		try (Reader reader = Files.newBufferedReader(file)) {
			Map<String, Object> rawTranslations = gson.fromJson(reader, new TypeToken<HashMap<String, Object>>() {
			}.getType());
			Map<String, String> translations = flattenMap(rawTranslations, dirName);
			translations.forEach((key, value) -> languageProvider.registerTranslation(key, langCode, value));
		} catch (IOException e) {
			logger.warning("Failed to read language file: " + file);
		}
		validateLanguage(langCode);
	}

	private JsonObject getJsonObjectFromFile(String filename) {
		try (Reader reader = Files.newBufferedReader(Paths.get(filename))) {
			return JsonParser.parseReader(reader).getAsJsonObject();
		} catch (IOException e) {
			logger.warning("Failed to read JSON file: " + filename);
			return new JsonObject();
		}
	}

	private void mergeJsonObjects(JsonObject source, JsonObject target) {
		for (String key : source.keySet()) {
			if (!target.has(key)) {
				target.add(key, source.get(key));
			} else {
				JsonElement sourceElement = source.get(key);
				JsonElement targetElement = target.get(key);
				if (sourceElement.isJsonObject() && targetElement.isJsonObject()) {
					mergeJsonObjects(sourceElement.getAsJsonObject(), targetElement.getAsJsonObject());
				}
			}
		}

		target.keySet().removeIf(key -> !source.has(key));
	}


	private Map<String, String> flattenMap(Map<String, Object> rawTranslations, String prefix) {
		Map<String, String> translations = new HashMap<>();
		for (Map.Entry<String, Object> entry : rawTranslations.entrySet()) {
			String key = prefix + "." + entry.getKey();
			if (entry.getValue() instanceof Map) {
				translations.putAll(flattenMap((Map<String, Object>) entry.getValue(), key));
			} else if (entry.getValue() instanceof List<?> list) {
				if (!list.isEmpty() && list.get(0) instanceof String) {
					String value = String.join("\n", (List<String>) list);
					translations.put(key, value);
				} else {
					for (Object obj : list) {
						if (obj instanceof Map) {
							translations.putAll(flattenMap((Map<String, Object>) obj, key));
						}
					}
				}
			} else {
				translations.put(key, entry.getValue().toString());
			}
		}
		return translations;
	}

	private void validateLanguage(String langCode) {
		Optional<Language> language = languageService.getLanguage(langCode);
		if (language.isEmpty()) {
			languageService.registerLanguage(
					new Language(Locale.forLanguageTag(langCode).getDisplayName(), langCode)
			);
		}
	}
}