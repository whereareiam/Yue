package com.aeritt.yue.core;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class SpringPluginManager extends org.pf4j.spring.SpringPluginManager {
	@Override
	@PostConstruct
	public void init() {
		// Do nothing
	}
}
