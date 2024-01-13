package com.reiko.nail.dto;

import lombok.Data;

@Data
public class SearchItemDto {

	/** 検索区分 */
	private String searchKbn;
	/** 商品コード  */
	private String shohinCd;
	/** カラー */
	private String color;
}
