package com.reiko.nail.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reiko.nail.enums.UpdateFlagEnum;

@Configuration
public class EnumConfigu {

	@Bean("deleteItem")
	public UpdateFlagEnum delete() {
		return UpdateFlagEnum.DELETE;
	}
	
	@Bean("updateItem")
	public UpdateFlagEnum update() {
		return UpdateFlagEnum.UPDATE;
	}
}
