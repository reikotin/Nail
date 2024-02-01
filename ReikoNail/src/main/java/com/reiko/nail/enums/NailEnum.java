package com.reiko.nail.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public interface NailEnum {
	
	@JsonValue
	public String getKey();
	
}
