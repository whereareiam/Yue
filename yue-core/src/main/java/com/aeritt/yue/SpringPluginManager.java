package com.aeritt.yue;

import org.springframework.stereotype.Service;

@Service
public class SpringPluginManager extends org.pf4j.spring.SpringPluginManager {
	@Override
	public void init() {
		// Do nothing
	}
}
