package com.reiko.nail.enums;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SeasonEnum implements NailEnum {

	SPRING("春", "SP"),   // P-WI-24O1
	SUMMER("夏", "SU"), // P-SP-24O1
	AUTUM("秋", "AU"),// P-SU-24O1
	WINTER("冬", "WI")   // P-AU-24
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
	
	private SeasonEnum (String key, String value) {
		_key = key;
		_value = value;
	}
	
	public static SeasonEnum getByKey(String key) {
		if(StringUtils.isEmpty(key)) {
			return null;
		}
		return Arrays.stream(SeasonEnum.values())
				.filter(e -> StringUtils.equals(key, e.getKey()))
				.findFirst()
				.orElse(null);
	}
}
