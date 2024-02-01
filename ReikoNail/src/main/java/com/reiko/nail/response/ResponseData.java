package com.reiko.nail.response;

import lombok.Data;

@Data
public class ResponseData<T> {
	
	private boolean hasShiire = false;

	private boolean hasError = false;
	
	private String message;
	
	private T data;
}
