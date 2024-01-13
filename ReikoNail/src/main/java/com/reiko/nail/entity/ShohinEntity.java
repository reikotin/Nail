package com.reiko.nail.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ShohinEntity {

	/** 商品コード */
	private String shohinCd;
	/** テーマカラー */
	private String color;
	/** 税抜額 */
	private int zeinukiGaku;
	/** 税額 */
	private int zeiGaku;
	/** 税込額 */
	private int zeikomiGaku;
	/** 作成所要時間 */
	private String makeTime;
	/** 材料メモ */
	private String zairyoMemo;
	/** 商品メモ */
	private String shohinMemo;
	/** 削除フラグ */
	private String deleteFlag;
	/** 登録日 */
	private LocalDate createDate;
	/** 更新日 */
	private LocalDate updateDate;
}
