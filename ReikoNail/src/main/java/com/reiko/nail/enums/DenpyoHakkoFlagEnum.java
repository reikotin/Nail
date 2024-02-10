package com.reiko.nail.enums;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DenpyoHakkoFlagEnum implements NailEnum {
	/** 0:受付中 */
	UKETSUKETYU("0", "受付中"),
	/** 1:発送 */
	HASSOZUMI("1", "発送済み"), 
	/** 2:削除 */
	DELETE("2", "削除")
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
	
	private DenpyoHakkoFlagEnum (String key, String value) {
		_key = key;
		_value = value;
	}
	
	public static DenpyoHakkoFlagEnum getByKey(String key) {
		if(StringUtils.isEmpty(key)) {
			return null;
		}
		return Arrays.stream(DenpyoHakkoFlagEnum.values())
				.filter(e -> StringUtils.equals(key, e.getKey()))
				.findFirst()
				.orElse(null);
	}
}
