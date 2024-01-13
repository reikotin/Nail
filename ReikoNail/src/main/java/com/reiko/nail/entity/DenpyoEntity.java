package com.reiko.nail.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DenpyoEntity {

	/** 伝票番号 */
	private String denpyoNo;
	/** お客様CD */
	private String customerCd;
	/** 購入日 */
	private LocalDate kounyuDate;
	/** 税抜額 */
	private Integer zeinukiGaku;
	/** 税額 */
	private Integer zeiGaku;
	/** 税込額 */
	private Integer zeikomiGaku;
	/** 伝票発行済みフラグ */
	private String denpyoHakkozumiFlag;
	/** 備考 */
	private String biko;
	/** 伝票作成日 */
	private LocalDate createDate;
	/** 伝票更新日 */
	private LocalDate updateDate;
}
