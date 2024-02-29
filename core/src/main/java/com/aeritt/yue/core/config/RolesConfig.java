package com.aeritt.yue.core.config;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class RolesConfig {
	private List<String> roles = new ArrayList<>();
	private String verifiedRole = "";
	private Map<String, String> languageRoles = new HashMap<>();
}
