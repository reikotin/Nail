package com.reiko.nail.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SearchShiireDto {
	
	private String janCd;

	private String daiBunruiName;
	
	private String shoBunruiName;
	
	private String itemName;
	
	private String shiireSaki;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
}
