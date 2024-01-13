package com.reiko.nail.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BunruiEntity {

	/** 分類コード */
	private String bunruiCd;
	/**大分類名 */
	private String daiBunruiName;
	/** 小分類名 */
	private String shoBunruiName;
	/** 削除フラグ */
	private String deleteFlag;
	/** 登録日 */
	private LocalDate createDate;
	/** 更新日 */
	private LocalDate updateDate;
    

}
