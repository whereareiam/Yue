package com.aeritt.yue.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.aeritt.yue.core.database.entity.Language;
import com.aeritt.yue.core.feature.verification.VerificationStep;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StepData {
	private User user;
	private Message message;
	private Language mainLanguage;
	private List<Language> additionalLanguages;
	private VerificationStep step;
	private boolean nextStep;
}
