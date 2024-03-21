package com.reiko.nail.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ShiireRirekiDto {

	private String rirekiId;
	
	private String janCd;
	
	private String daiBunruiName;
	
	private String shoBunruiName;
	
	private String itemName;
	
	private String shiireSaki;
	
	private String shiireMaker;
	
	private Integer zeikomiGaku;
	
	private LocalDate shiireDate;
	/**
	SR.RIREKI_ID AS RIREKI_ID,
	NS.JAN_CD AS JAN_CD,
	NS.DAI_BUNRUI_NAME AS DAI_BUNRUI_NAME,
	NS.SHO_BUNRUI_NAME AS SHO_BUNRUI_NAME,
	NS.ITEM_NAME AS ITEM_NAME,
	NS.SHIIRE_SAKI AS SHIIRE_SAKI,
	NS.SHIIRE_MAKER AS SHIIRE_MAKER,
	NS.ITEM_SIZE AS ITEM_SIZE,
	NS.ZEIKOMIGAKU AS ZEIKOMIGAKU,
	NS.UPDATE_DATE AS UPDATE_DATE*/
}
