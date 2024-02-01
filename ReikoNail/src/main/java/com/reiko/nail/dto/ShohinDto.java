package com.reiko.nail.dto;

import java.util.List;

import com.reiko.nail.enums.SeasonEnum;
import com.reiko.nail.enums.ThemeTypeEnum;

import lombok.Data;

@Data
public class ShohinDto {

	/** 商品CD */
	private String shohinCd;
	/** テーマ */
	private ThemeTypeEnum themeType;
	/** 季節 */
	private SeasonEnum season;
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
