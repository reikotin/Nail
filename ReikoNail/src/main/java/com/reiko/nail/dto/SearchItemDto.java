package com.reiko.nail.dto;

import com.reiko.nail.enums.ThemeTypeEnum;

import lombok.Data;

@Data
public class SearchItemDto {

	/** 検索区分 */
	private String searchKbn;
	/** テーマタイプ */
	private ThemeTypeEnum themeType;
	/** 季節 */
	private String seasonCd;
}
