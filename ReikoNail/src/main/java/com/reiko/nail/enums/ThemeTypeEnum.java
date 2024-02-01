package com.reiko.nail.enums;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ThemeTypeEnum implements NailEnum {

	KAWAII("かわいい", "P"),   // P-WI24-O1
	KIRAKIRA("きらきら", "G"), // P-SP-24O1
	KAKKOII("かっこいい", "C"),// P-SU-24O1
	URUURU("うるうる", "U"),   // P-AU-24O1
	NYUANCE("ニュアンス", "N")  // N-25SPO3
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
	
	private ThemeTypeEnum (String key, String value) {
		_key = key;
		_value = value;
	}
	
	public static ThemeTypeEnum getByKey(String key) {
		if(StringUtils.isEmpty(key)) {
			return null;
		}
		return Arrays.stream(ThemeTypeEnum.values())
				.filter(e -> StringUtils.equals(key, e.getKey()))
				.findFirst()
				.orElse(null);
	}

	
}
