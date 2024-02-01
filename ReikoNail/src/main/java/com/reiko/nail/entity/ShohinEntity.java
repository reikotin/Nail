package com.reiko.nail.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ShohinEntity {

	/** 商品コード */
	private String shohinCd; // P G C U N 
	/** テーマタイプ */
	private String themeType; // かわいい、かっこいい、ニュアンス、うるうる、きらきら
	/** 季節 */
	private String season;
	/** 税抜額 */
	private int zeinukiGaku;
	/** 税額 */
	private int zeiGaku;
	/** 税込額 */
	private int zeikomiGaku;
	/** 作成時間 */
	private String makeTime;
	/** 仕入IDリスト */
	private String shiireIdList;
	/** 商品メモ */
	private String shohinMemo;
	/** 削除フラグ */
	private String deleteFlag;
	/** 登録日 */
	private LocalDate createDate;
	/** 更新日 */
	private LocalDate updateDate;
}
