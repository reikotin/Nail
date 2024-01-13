package com.reiko.nail.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ShiireEntity {

	private String daiBunruiName;
	
	private String shoBunruiName;
	
	private String itemName;
	
	private int zeinukiGaku;
	
	private int zeigaku;
	
	private int zeikomiGaku;
	
	private LocalDate shiireDate;
	
	private LocalDate createDate;
	
	private LocalDate updateDate;
}
