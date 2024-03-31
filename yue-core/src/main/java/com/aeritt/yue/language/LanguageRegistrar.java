package com.aeritt.yue.language;

import com.aeritt.yue.api.model.Language;
import com.google.gson.Gson;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Service
public class LanguageRegistrar {
	private final Logger logger;
	private final LanguageService languageService;
	private final Path langPath;

	@Autowired
	public LanguageRegistrar(Logger logger, LanguageService languageService,
	                         @Qualifier("languagePath") Path langPath) {
		this.logger = logger;
		this.languageService = languageService;
		this.langPath = langPath;
	}

	@Async
	@Order(Ordered.HIGHEST_PRECEDENCE)
	@EventListener(ApplicationStartedEvent.class)
	public void registerTranslations() {
		try (Stream<Path> paths = Files.walk(langPath, 1)) {
			paths.filter(Files::isDirectory).forEach(dir -> {
				String dirName = dir.getFileName().toString();
				try (Stream<Path> files = Files.walk(dir, 1)) {
					files.filter(file -> file.toString().endsWith(".json")).forEach(file -> {
						String langCode = file.getFileName().toString().replace(".json", "");
						try (Reader reader = Files.newBufferedReader(file)) {
							Map<String, Object> rawTranslations = new Gson().fromJson(reader, new TypeToken<HashMap<String, Object>>() {
							}.getType());
							Map<String, String> translations = flattenMap(rawTranslations, dirName);
							translations.forEach((key, value) -> languageService.registerTranslation(key, langCode, value));
						} catch (IOException e) {
							logger.warning("Failed to read language file: " + file);
						}

						validateLanguage(langCode);
					});
				} catch (IOException e) {
					logger.warning("Failed to read language directory: " + dir);
				}
			});
		} catch (IOException e) {
			logger.warning("Failed to read language directory: " + langPath);
		}
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
		Language language = languageService.getLanguage(langCode);
		if (language == null) {
			languageService.registerLanguage(
					new Language(Locale.forLanguageTag(langCode).getDisplayName(), langCode)
			);
		}
	}
}