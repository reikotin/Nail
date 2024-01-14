package com.reiko.nail.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ShiireEntity {

	private String daiBunruiName;
	
	private String shoBunruiName;
	
	private String itemName;
	
	private String zeinukiGaku;
	
	private String zeigaku;
	
	private String zeikomiGaku;
	
	private String shiireDate;
	
	private LocalDate createDate;
	
	private LocalDate updateDate;
}
