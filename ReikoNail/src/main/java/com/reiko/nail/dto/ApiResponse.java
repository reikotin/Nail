package com.reiko.nail.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ApiResponse {
	private String message;
    private List<AddressDto> results;
    private int status;
    private boolean error;
    // コンストラクタ
    public ApiResponse(@JsonProperty("message") String message,
            @JsonProperty("results") List<AddressDto> results,
            @JsonProperty("status") int status) {
	this.message = message;
	this.results = results;
	this.status = status;
	}
    

}
