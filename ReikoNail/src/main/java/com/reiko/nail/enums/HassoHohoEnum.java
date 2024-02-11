package com.reiko.nail.enums;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonValue;

public enum HassoHohoEnum implements NailEnum {
	
	
	QUICKPOST("01", "クイックポスト"),
//	YAMATO("2", "ヤマト")
	;

	private final String _key;
	
	private final String _value;
	
	@Override
	@JsonValue
	public String getKey() {
		return _key;
	}
	
	public String getValue() {
		return _value; 
	}
	
	private HassoHohoEnum (String key, String value) {
		_key = key;
		_value = value;
	}
	
	public static HassoHohoEnum getByKey(String key) {
		if(StringUtils.isEmpty(key)) {
			return null;
		}
		return Arrays.stream(HassoHohoEnum.values())
				.filter(e -> StringUtils.equals(key, e.getKey()))
				.findFirst()
				.orElse(null);
	}

}
