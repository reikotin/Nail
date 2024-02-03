package com.reiko.nail.dto;

import java.util.List;

import lombok.Data;

@Data
public class UpdateShohinDto {

	/** 更新フラグ */
	private String updateFlag;
	/** 商品CD */
	private String shohinCd;
	/** テーマ */
	private String themeType;
	/** 季節 */
	private String season;
	/** 税抜額 */
	private Integer zeinukiGaku;
	/** 税額 */
	private Integer zeiGaku;
	/** 税込額 */
	private Integer zeikomiGaku;
	/** 仕入商品リスト */
	private List<ShohinIdDto> idList;
	/** 作成時間 */
	private String makeTime;
	/** 商品詳細 */
	private String shohinMemo;
}
