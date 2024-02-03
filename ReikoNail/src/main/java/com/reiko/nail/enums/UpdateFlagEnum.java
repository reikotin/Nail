package com.reiko.nail.enums;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UpdateFlagEnum implements NailEnum {
	
	DELETE("削除", 0),
	UPDATE("更新", 1), 
	;

	private final String _key;
	
	private final int _value;
	
	@Override
	@JsonValue
	public String getKey() {
		return _key;
	}
	
	public int getValue() {
		return _value; 
	}
	
	private UpdateFlagEnum (String key, int value) {
		_key = key;
		_value = value;
	}
	
	public static UpdateFlagEnum getByKey(String key) {
		if(StringUtils.isEmpty(key)) {
			return null;
		}
		return Arrays.stream(UpdateFlagEnum.values())
				.filter(e -> StringUtils.equals(key, e.getKey()))
				.findFirst()
				.orElse(null);
	}
}
