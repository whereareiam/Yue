package me.whereareiam.yue.api;

import lombok.Getter;

public class YueAPI {
	@Getter
	private static Yue instance;

	public YueAPI(Yue instance) {
		YueAPI.instance = instance;
	}
}
