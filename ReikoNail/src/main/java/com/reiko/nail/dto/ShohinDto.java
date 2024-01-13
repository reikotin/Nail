package com.reiko.nail.dto;

import lombok.Data;

@Data
public class ShohinDto {

	/** 商品CD */
	private String shohinCd;
	/** テーマカラー */
	private String color;
	/** 税抜額 */
	private Integer zeinukiGaku = 0;
	/** 税額 */
	private Integer zeiGaku = 0;
	/** 税込額 */
	private Integer zeikomiGaku = 0;
	/** 作成所要時間 */
	private String makeTime;
	/** 材料メモ */
	private String zairyoMemo;
	/** 商品詳細 */
	private String shohinMemo;
}
