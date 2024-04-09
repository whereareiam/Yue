package com.aeritt.yue.api.event.internal.language;


import com.aeritt.yue.api.event.Event;
import com.aeritt.yue.api.model.Language;
import com.aeritt.yue.api.model.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class LanguageChangeEvent extends Event {
	private final UserProfile userProfile;
	private final Optional<Language> oldLanguage;
}
