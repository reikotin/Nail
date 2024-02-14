package com.reiko.nail.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ExportDenpyo {
	/** ナンバー */
	private Integer number;
	/** 伝票番号 */
	private String denpyoNo;
	/** 姓 */
	private String customerSei;
	/** 名 */
	private String customerMei;
	/** 購入日(注文日) */
	private LocalDate kounyuDate;
	/** 発送日 */
	private LocalDate hassoDate;
	/** 商品番号 */
	private String shohinCd;
	/** 税込額 */
	private Integer zeikomiGaku;
	/** 数量 */
 	private Integer suryo;
	/** 小計 */
	private Integer shokei;
	
}
